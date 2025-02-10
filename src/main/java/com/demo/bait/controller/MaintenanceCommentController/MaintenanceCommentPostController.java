package com.demo.bait.controller.MaintenanceCommentController;

import com.demo.bait.dto.MaintenanceCommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.MaintenanceCommentService.MaintenanceCommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/maintenance-comment")
public class MaintenanceCommentPostController {

    public final MaintenanceCommentService maintenanceCommentService;

    @PostMapping("/add")
    public ResponseDTO addMaintenanceComment(@RequestBody MaintenanceCommentDTO maintenanceCommentDTO) {
        return maintenanceCommentService.addMaintenanceComment(maintenanceCommentDTO);
    }
}
