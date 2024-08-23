package com.demo.bait.controller.FileUploadController;

import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class FileUploadGetController {

    public final FileUploadService fileUploadService;

    @GetMapping("/thumbnail/{fileId}")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Integer fileId) {
        return fileUploadService.getThumbnail(fileId);
    }

    @GetMapping("/all")
    public List<FileUploadDTO> getAllFiles() {
        return fileUploadService.getAllFiles();
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId) {
        return fileUploadService.downloadFile(fileId);
    }

    @GetMapping("/{fileId}")
    public FileUploadDTO getFileById(@PathVariable Integer fileId) {
        return fileUploadService.getFileById(fileId);
    }
}
