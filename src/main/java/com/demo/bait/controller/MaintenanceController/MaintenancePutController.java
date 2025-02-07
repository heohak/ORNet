package com.demo.bait.controller.MaintenanceController;

import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.MaintenanceServices.MaintenanceFileUploadService;
import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/maintenance")
public class MaintenancePutController {

    public final MaintenanceService maintenanceService;
    public final MaintenanceFileUploadService maintenanceFileUploadService;

    @PutMapping("/upload/{id}")
    public ResponseDTO uploadFileToMaintenance(@PathVariable Integer id,
                                               @RequestParam("files") List<MultipartFile> files) throws IOException {
        return maintenanceFileUploadService.uploadFilesToMaintenance(id, files);
    }

    @PutMapping("/update/{maintenanceId}")
    public ResponseDTO updateMaintenance(@PathVariable Integer maintenanceId,
                                         @RequestBody MaintenanceDTO maintenanceDTO) {
        return maintenanceService.updateMaintenance(maintenanceId, maintenanceDTO);
    }

    @PutMapping("/time/{maintenanceId}")
    public ResponseDTO addTimeToMaintenance(@PathVariable Integer maintenanceId,
                                            @RequestParam(value = "hours", required = false) Integer hours,
                                            @RequestParam(value = "minutes", required = false) Integer minutes) {
        return maintenanceService.addTimeSpentToMaintenance(maintenanceId, hours, minutes);
    }
}
