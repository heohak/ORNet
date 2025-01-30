package com.demo.bait.controller.MaintenanceController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.service.MaintenanceServices.MaintenanceFileUploadService;
import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/maintenance")
public class MaintenanceGetController {

    public final MaintenanceService maintenanceService;
    public final MaintenanceFileUploadService maintenanceFileUploadService;
    private final RequestParamParser requestParamParser;


    @GetMapping("/all")
    public List<MaintenanceDTO> getAllMaintenances() {
        return maintenanceService.getAllMaintenances();
    }

    @GetMapping("/files/{maintenanceId}")
    public List<FileUploadDTO> getMaintenanceFiles(@PathVariable String maintenanceId) {
        Integer parsedMaintenanceId = requestParamParser.parseId(maintenanceId, "Maintenance ID");
        return maintenanceFileUploadService.getMaintenanceFiles(parsedMaintenanceId);
    }

    @GetMapping("/{maintenanceId}")
    public MaintenanceDTO getMaintenanceById(@PathVariable String maintenanceId) {
        Integer parsedMaintenanceId = requestParamParser.parseId(maintenanceId, "Maintenance ID");
        return maintenanceService.getMaintenanceById(parsedMaintenanceId);
    }
}
