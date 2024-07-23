package com.demo.bait.controller;

import com.demo.bait.service.FileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class FileUploadController {

    public final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
            fileUploadService.uploadFiles(files);
            return ResponseEntity.status(HttpStatus.OK).body("Files uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload files");
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Integer fileId) {
        return fileUploadService.getThumbnail(fileId);
    }
}
