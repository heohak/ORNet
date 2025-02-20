package com.demo.bait.controller.MaintenanceController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.MaintenanceServices.MaintenanceFileUploadService;
import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/maintenance")
public class MaintenanceDeleteController {

    public final MaintenanceService maintenanceService;
    private final RequestParamParser requestParamParser;

    @DeleteMapping("/{maintenanceId}")
    public ResponseDTO deleteMaintenanceById(@PathVariable String maintenanceId) {
        Integer parsedMaintenanceId = requestParamParser.parseId(maintenanceId, "Maintenance ID");
        return maintenanceService.deleteMaintenanceById(parsedMaintenanceId);
    }
}
