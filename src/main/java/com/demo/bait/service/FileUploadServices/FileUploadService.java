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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class FileUploadService {

    private FileUploadRepo fileUploadRepo;
    private FileUploadMapper fileUploadMapper;

    private static final String UPLOAD_DIR = "uploads";

    @Transactional
    public Set<FileUpload> uploadFiles(List<MultipartFile> files) throws IOException {
        Set<FileUpload> uploadedFiles = new HashSet<>();
        for (MultipartFile file : files) {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            Path filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Thumbnailator
            byte[] thumbnail = generateThumbnail(file);

            FileUpload fileUpload = new FileUpload();
            fileUpload.setFileName(file.getOriginalFilename());
            fileUpload.setFilePath(filePath.toString());
            fileUpload.setFileSize(file.getSize());
            fileUpload.setFileType(file.getContentType());
            fileUpload.setThumbnail(thumbnail);

            fileUploadRepo.save(fileUpload);
            uploadedFiles.add(fileUpload);
        }
        return uploadedFiles;
    }

    private byte[] generateThumbnail(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
                Thumbnails.of(file.getInputStream())
                        .size(150, 150)
                        .outputFormat("jpg")
                        .toOutputStream(thumbnailOutputStream);
                return thumbnailOutputStream.toByteArray();
            } else {
                return generatePlaceholderImage(file.getOriginalFilename(), contentType);
            }
        } catch (IOException e) {
            log.error("Error generating thumbnail for file: {}", file.getOriginalFilename(), e);
            return new byte[0];
        }
    }

    private byte[] generatePlaceholderImage(String fileName, String contentType) {
        try {
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
        FileUpload fileUpload = fileUploadRepo.findById(fileId).orElseThrow(() -> new RuntimeException("File not found"));
        byte[] thumbnail = fileUpload.getThumbnail();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "image/jpeg");

        return new ResponseEntity<>(thumbnail, headers, HttpStatus.OK);
    }

    public List<FileUploadDTO> getAllFiles() {
        return fileUploadMapper.toDtoList(fileUploadRepo.findAll());
    }

    public ResponseEntity<Resource> downloadFile(Integer fileId) {
        return prepareFileResponse(fileId, "attachment");
    }

    public ResponseEntity<Resource> openFileInBrowser(Integer fileId) {
        return prepareFileResponse(fileId, "inline");
    }

    private ResponseEntity<Resource> prepareFileResponse(Integer fileId, String dispositionType) {
        FileUpload fileUpload = fileUploadRepo.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        Path path = Paths.get(fileUpload.getFilePath());
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not found or not readable");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error loading file", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, dispositionType + "; filename=\"" + fileUpload.getFileName() + "\"");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileUpload.getFileSize())
                .contentType(MediaType.parseMediaType(fileUpload.getFileType()))
                .body(resource);
    }

    public Set<FileUpload> fileIdsToFilesSet(List<Integer> fileIds) {
        Set<FileUpload> files = new HashSet<>();
        for (Integer fileId : fileIds) {
            FileUpload file = fileUploadRepo.findById(fileId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid file ID: " + fileId));
            files.add(file);
        }
        return files;
    }

    public FileUploadDTO getFileById(Integer id) {
        Optional<FileUpload> fileOpt = fileUploadRepo.findById(id);
        if (fileOpt.isEmpty()) {
            throw new EntityNotFoundException("File with ID " + id + " not found");
        }
        FileUpload file = fileOpt.get();
        return fileUploadMapper.toDto(file);
    }
}
