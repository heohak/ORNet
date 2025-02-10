package com.demo.bait.controller.MaintenanceCommentController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.MaintenanceCommentDTO;
import com.demo.bait.service.MaintenanceCommentService.MaintenanceCommentFileUploadService;
import com.demo.bait.service.MaintenanceCommentService.MaintenanceCommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/maintenance-comment")
public class MaintenanceCommentGetController {

    public final MaintenanceCommentFileUploadService maintenanceCommentFileUploadService;
    public final MaintenanceCommentService maintenanceCommentService;
    private final RequestParamParser requestParamParser;

    @GetMapping("/{maintenanceCommentId}")
    public MaintenanceCommentDTO getMaintenanceCommentById(@PathVariable String maintenanceCommentId) {
        Integer parsedMaintenanceCommentId = requestParamParser.parseId(maintenanceCommentId,
                "Maintenance Comment ID");
        return maintenanceCommentService.getMaintenanceCommentById(parsedMaintenanceCommentId);
    }

    @GetMapping("/files/{maintenanceCommentId}")
    public List<FileUploadDTO> getMaintenanceCommentFiles(@PathVariable String maintenanceCommentId) {
        Integer parsedMaintenanceCommentId = requestParamParser.parseId(maintenanceCommentId,
                "Maintenance Comment ID");
        return maintenanceCommentFileUploadService.getMaintenanceCommentFiles(parsedMaintenanceCommentId);
    }

    @GetMapping("/maintenance/{maintenanceId}")
    public List<MaintenanceCommentDTO> getMaintenanceCommentsForMaintenance(@PathVariable String maintenanceId) {
        Integer parsedMaintenanceId = requestParamParser.parseId(maintenanceId, "Maintenance ID");
        return maintenanceCommentService.getMaintenanceCommentsForMaintenance(parsedMaintenanceId);
    }
}
