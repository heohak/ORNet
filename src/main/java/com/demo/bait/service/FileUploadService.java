package com.demo.bait.service;

import com.demo.bait.entity.FileUpload;
import com.demo.bait.repository.FileUploadRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class FileUploadService {

    private FileUploadRepo fileUploadRepo;

    private static final String UPLOAD_DIR = "uploads/";

    public Set<FileUpload> uploadFiles(List<MultipartFile> files) throws IOException {
        Set<FileUpload> uploadFileIds = new HashSet<>();
        for (MultipartFile file : files) {
            FileUpload fileUpload = new FileUpload();
            fileUpload.setFileName(file.getOriginalFilename());
            fileUpload.setFileSize(file.getSize());

            // Ensure the upload directory exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            Path filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath);

            fileUpload.setFilePath(filePath.toString());
            fileUploadRepo.save(fileUpload);
            uploadFileIds.add(fileUpload);
        }
        return uploadFileIds;
    }
}
