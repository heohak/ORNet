package com.demo.bait.controller.MaintenanceController;

import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/maintenance")
public class MaintenancePostController {

    public final MaintenanceService maintenanceService;

    @PostMapping("/add")
    public ResponseDTO addMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) {
        return maintenanceService.addMaintenance(maintenanceDTO);
    }
}
