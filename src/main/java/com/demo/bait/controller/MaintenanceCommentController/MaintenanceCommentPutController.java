package com.demo.bait.controller.MaintenanceCommentController;

import com.demo.bait.dto.MaintenanceCommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.MaintenanceCommentService.MaintenanceCommentFileUploadService;
import com.demo.bait.service.MaintenanceCommentService.MaintenanceCommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/maintenance-comment")
public class MaintenanceCommentPutController {

    public final MaintenanceCommentService maintenanceCommentService;

    public final MaintenanceCommentFileUploadService maintenanceCommentFileUploadService;

    @PutMapping("/update/{maintenanceCommentId}")
    public ResponseDTO updateMaintenanceComment(@PathVariable Integer maintenanceCommentId,
                                                @RequestBody MaintenanceCommentDTO maintenanceCommentDTO) {
        return maintenanceCommentService.updateMaintenanceComment(maintenanceCommentId, maintenanceCommentDTO);
    }

    @PutMapping("/upload/{maintenanceCommentId}")
    public ResponseDTO uploadFilesToMaintenanceComment(@PathVariable Integer maintenanceCommentId,
                                                       @RequestParam("files") List<MultipartFile> files)
            throws IOException {
        return maintenanceCommentFileUploadService.uploadFilesToMaintenanceComment(maintenanceCommentId, files);
    }
}
