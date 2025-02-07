package com.demo.bait.controller.MaintenanceController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.service.MaintenanceServices.MaintenanceFileUploadService;
import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import com.demo.bait.service.MaintenanceServices.MaintenanceSpecificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/api/maintenance")
public class MaintenanceGetController {

    public final MaintenanceService maintenanceService;
    public final MaintenanceFileUploadService maintenanceFileUploadService;
    private final RequestParamParser requestParamParser;
    private final MaintenanceSpecificationService maintenanceSpecificationService;


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

    @GetMapping("/search")
    public List<MaintenanceDTO> searchAndFilterMaintenances(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "locationId", required = false) Integer locationId,
            @RequestParam(value = "baitWorkerId", required = false) Integer baitWorkerId,
            @RequestParam(value = "deviceId", required = false) Integer deviceId,
            @RequestParam(value = "linkedDeviceId", required = false) Integer linkedDeviceId,
            @RequestParam(value = "softwareId", required = false) Integer softwareId,
            @RequestParam(value = "clientId", required = false) Integer clientId) {
        return maintenanceSpecificationService.searchAndFilterMaintenances(query, locationId, baitWorkerId, deviceId,
                linkedDeviceId, softwareId, clientId);
    }

    @GetMapping("/connections/{maintenanceId}")
    public Map<String, List<?>> getMaintenanceConnectionsMap(@PathVariable String maintenanceId) {
        Integer parsedMaintenanceId = requestParamParser.parseId(maintenanceId, "Maintenance ID");
        return maintenanceService.getMaintenanceConnectionsMap(parsedMaintenanceId);
    }
}
