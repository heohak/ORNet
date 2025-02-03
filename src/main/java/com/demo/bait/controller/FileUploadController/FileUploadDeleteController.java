package com.demo.bait.controller.FileUploadController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/file")
public class FileUploadDeleteController {

    public final FileUploadService fileUploadService;

    @DeleteMapping("/{fileId}")
    public ResponseDTO deleteFileById(@PathVariable Integer fileId) {
        return fileUploadService.deleteFileById(fileId);
    }
}
