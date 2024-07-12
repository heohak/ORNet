package com.demo.bait.controller;

import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.MaintenanceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/maintenance")
public class MaintenanceController {

    public final MaintenanceService maintenanceService;

    @PostMapping("/add")
    public ResponseDTO addMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) {
        return maintenanceService.addMaintenance(maintenanceDTO);
    }

    @GetMapping("/all")
    public List<MaintenanceDTO> getAllMaintenances() {
        return maintenanceService.getAllMaintenances();
    }

    @PutMapping("/upload/{id}")
    public ResponseDTO uploadFileToMaintenance(@PathVariable Integer id, @RequestParam("files") List<MultipartFile> files)
            throws IOException {
        return maintenanceService.uploadFilesToMaintenance(id, files);
    }
}
