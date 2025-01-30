package com.demo.bait.service.FileUploadServices;

import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.mapper.FileUploadMapper;
import com.demo.bait.repository.FileUploadRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FileUploadService {

    private final FileUploadRepo fileUploadRepo;
    private final FileUploadMapper fileUploadMapper;
    private static final String UPLOAD_DIR = "uploads";

    @Transactional
    public Set<FileUpload> uploadFiles(List<MultipartFile> files) throws IOException {
        log.info("Uploading {} files", files.size());
        try {
            Set<FileUpload> uploadedFiles = new HashSet<>();
            File uploadDir = new File(UPLOAD_DIR);

            if (!uploadDir.exists()) {
                log.debug("Creating upload directory: {}", UPLOAD_DIR);
                uploadDir.mkdirs();
            }

            for (MultipartFile file : files) {
                log.debug("Processing file: {}", file.getOriginalFilename());
                String uniqueFileName = UUID.randomUUID().toString();
                String fileExtension = getFileExtension(file.getOriginalFilename());
                String storedFileName = uniqueFileName + fileExtension;

                Path filePath = Paths.get(UPLOAD_DIR, storedFileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                byte[] thumbnail = generateThumbnail(file);

                FileUpload fileUpload = new FileUpload();
                fileUpload.setFileName(file.getOriginalFilename());
                fileUpload.setStoredFileName(storedFileName);
                fileUpload.setFilePath(filePath.toString());
                fileUpload.setFileSize(file.getSize());
                fileUpload.setFileType(file.getContentType());
                fileUpload.setThumbnail(thumbnail);

                fileUploadRepo.save(fileUpload);
                uploadedFiles.add(fileUpload);

                log.info("File {} uploaded successfully as {}", file.getOriginalFilename(), storedFileName);
            }

            log.info("{} files uploaded successfully", uploadedFiles.size());
            return uploadedFiles;
        } catch (IOException e) {
            log.error("Error during file upload", e);
            throw e;
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            log.debug("No file extension found for file: {}", fileName);
            return "";
        }
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        log.debug("Extracted file extension: {}", extension);
        return extension;
    }

    private byte[] generateThumbnail(MultipartFile file) {
        try {
            log.debug("Generating thumbnail for file: {}", file.getOriginalFilename());
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
                Thumbnails.of(file.getInputStream())
                        .size(150, 150)
                        .outputFormat("jpg")
                        .toOutputStream(thumbnailOutputStream);
                return thumbnailOutputStream.toByteArray();
            } else {
                log.debug("File {} is not an image. Generating placeholder.", file.getOriginalFilename());
                return generatePlaceholderImage(file.getOriginalFilename());
            }
        } catch (IOException e) {
            log.error("Error generating thumbnail for file: {}", file.getOriginalFilename(), e);
            return new byte[0];
        }
    }

    private byte[] generatePlaceholderImage(String fileName) {
        try {
            log.debug("Generating placeholder image for file: {}", fileName);
            BufferedImage placeholder = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = placeholder.createGraphics();
            g2d.setColor(Color.GRAY);
            g2d.fillRect(0, 0, 150, 150);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));

            String fileExtension = fileName != null ? fileName
                    .substring(fileName.lastIndexOf('.') + 1).toUpperCase() : "FILE";
            g2d.drawString(fileExtension, 40, 80);
            g2d.dispose();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(placeholder, "jpg", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("Error generating placeholder image", e);
            return new byte[0];
        }
    }

    public ResponseEntity<byte[]> getThumbnail(Integer fileId) {
        if (fileId == null) {
            log.warn("File ID is null, returning bad request.");
            return ResponseEntity.badRequest()
                    .body(null);
        }

        log.info("Fetching thumbnail for file ID: {}", fileId);
        FileUpload fileUpload = fileUploadRepo.findById(fileId)
                .orElseThrow(() -> {
                    log.warn("File with ID {} not found", fileId);
                    return new RuntimeException("File not found");
                });
        byte[] thumbnail = fileUpload.getThumbnail();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "image/jpeg");

        log.info("Thumbnail fetched successfully for file ID: {}", fileId);
        return new ResponseEntity<>(thumbnail, headers, HttpStatus.OK);
    }

    public List<FileUploadDTO> getAllFiles() {
        log.info("Fetching all uploaded files");
        List<FileUploadDTO> files = fileUploadMapper.toDtoList(fileUploadRepo.findAll());
        log.info("Fetched {} files", files.size());
        return files;
    }

    public ResponseEntity<Resource> downloadFile(Integer fileId) {
        if (fileId == null) {
            log.warn("File ID is null, returning bad request.");
            return ResponseEntity.badRequest()
                    .body(null);
        }

        log.info("Preparing download for file ID: {}", fileId);
        return prepareFileResponse(fileId, "attachment");
    }

    public ResponseEntity<Resource> openFileInBrowser(Integer fileId) {
        if (fileId == null) {
            log.warn("File ID is null, returning bad request.");
            return ResponseEntity.badRequest()
                    .body(null);
        }

        log.info("Preparing to open file in browser for file ID: {}", fileId);
        return prepareFileResponse(fileId, "inline");
    }

    private ResponseEntity<Resource> prepareFileResponse(Integer fileId, String dispositionType) {
        log.debug("Preparing file response for file ID: {} with disposition: {}", fileId, dispositionType);
        FileUpload fileUpload = fileUploadRepo.findById(fileId)
                .orElseThrow(() -> {
                    log.warn("File with ID {} not found", fileId);
                    return new RuntimeException("File not found");
                });

        Path path = Paths.get(fileUpload.getFilePath());
        Resource resource = loadResource(path);
        HttpHeaders headers = createHeaders(fileUpload.getFileName(), dispositionType);

        log.info("File response prepared for file ID: {}", fileId);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileUpload.getFileSize())
                .contentType(MediaType.parseMediaType(fileUpload.getFileType()))
                .body(resource);
    }

    public static Resource loadResource(Path path) {
        try {
            log.debug("Loading resource from path: {}", path);
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                log.error("File at path {} not found or not readable", path);
                throw new RuntimeException("File not found or not readable");
            }
            return resource;
        } catch (MalformedURLException e) {
            log.error("Error loading file at path: {}", path, e);
            throw new RuntimeException("Error loading file", e);
        }
    }

    public static HttpHeaders createHeaders(String fileName, String dispositionType) {
        log.debug("Creating headers for file: {} with disposition: {}", fileName, dispositionType);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, dispositionType + "; filename=\"" + fileName + "\"");
        return headers;
    }

    public Set<FileUpload> fileIdsToFilesSet(List<Integer> fileIds) {
        log.info("Fetching files for IDs: {}", fileIds);
        Set<FileUpload> files = new HashSet<>();
        for (Integer fileId : fileIds) {
            FileUpload file = fileUploadRepo.findById(fileId)
                    .orElseThrow(() -> {
                        log.warn("Invalid file ID: {}", fileId);
                        return new IllegalArgumentException("Invalid file ID: " + fileId);
                    });
            files.add(file);
        }
        log.info("Fetched {} files for given IDs", files.size());
        return files;
    }

    public FileUploadDTO getFileById(Integer id) {
        if (id == null) {
            log.warn("File ID is null. Returning null");
            return null;
        }

        log.info("Fetching file with ID: {}", id);
        Optional<FileUpload> fileOpt = fileUploadRepo.findById(id);
        if (fileOpt.isEmpty()) {
            log.warn("File with ID {} not found", id);
            throw new EntityNotFoundException("File with ID " + id + " not found");
        }
        FileUpload file = fileOpt.get();
        log.info("File with ID {} fetched successfully", id);
        return fileUploadMapper.toDto(file);
    }
}
