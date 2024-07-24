package com.demo.bait.service;

import com.demo.bait.entity.FileUpload;
import com.demo.bait.repository.FileUploadRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class FileUploadService {

    private FileUploadRepo fileUploadRepo;

    private static final String UPLOAD_DIR = "uploads/";

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
//            fileUpload.setUploadTime(Timestamp.valueOf(LocalDateTime.now()));

            fileUploadRepo.save(fileUpload);
            uploadedFiles.add(fileUpload);
        }
        return uploadedFiles;
    }

    private byte[] generateThumbnail(MultipartFile file) {
        try {
            ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(150, 150)  // width and height of the thumbnail
                    .outputFormat("jpg")
                    .toOutputStream(thumbnailOutputStream);
            return thumbnailOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("Error generating thumbnail for file: {}", file.getOriginalFilename(), e);
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
}
