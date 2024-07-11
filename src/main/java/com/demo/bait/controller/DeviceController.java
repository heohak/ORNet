package com.demo.bait.controller;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class DeviceController {

    public final DeviceService deviceService;

    @PostMapping("/device")
    public ResponseDTO addDevice(@RequestBody DeviceDTO deviceDTO) {
        return deviceService.addDevice(deviceDTO);
    }

    @GetMapping("/devices/{clientId}")
    public List<DeviceDTO> getDevicesByClientId(@PathVariable Integer clientId) {
        return deviceService.getDevicesByClientId(clientId);
    }

    @GetMapping("/devices")
    public List<DeviceDTO> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/device/{deviceId}")
    public DeviceDTO getDeviceById(@PathVariable Integer deviceId) {
        return deviceService.getDeviceById(deviceId);
    }

    @DeleteMapping("/device/{deviceId}")
    public ResponseDTO deleteDevice(@PathVariable Integer deviceId) {
        return deviceService.deleteDevice(deviceId);
    }

    @PutMapping("/device/maintenance/{deviceId}/{maintenanceId}")
    public ResponseDTO addMaintenance(@PathVariable Integer deviceId, @PathVariable Integer maintenanceId) {
        return deviceService.addMaintenanceToDevice(deviceId, maintenanceId);
    }

    @PutMapping("/device/upload/{deviceId}")
    public ResponseDTO uploadFile(@PathVariable Integer deviceId, @RequestParam("files") List<MultipartFile> files)
            throws IOException {
        return deviceService.uploadFilesToDevice(deviceId, files);
    }

    @PutMapping("/device/location/{deviceId}/{locationId}")
    public ResponseDTO addLocation(@PathVariable Integer deviceId, @PathVariable Integer locationId) {
        return deviceService.addLocationToDevice(deviceId, locationId);
    }

    @GetMapping("/device/maintenances/{deviceId}")
    public List<MaintenanceDTO> getMaintenances(@PathVariable Integer deviceId) {
        return deviceService.getDeviceMaintenances(deviceId);
    }

    @PutMapping("/device/client/{deviceId}/{clientId}")
    public ResponseDTO addClient(@PathVariable Integer deviceId, @PathVariable Integer clientId) {
        return deviceService.addClientToDevice(deviceId, clientId);
    }

    // written off date PUT endpoint
}
