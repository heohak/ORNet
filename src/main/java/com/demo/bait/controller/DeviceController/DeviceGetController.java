package com.demo.bait.controller.DeviceController;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.service.DeviceServices.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/device")
public class DeviceGetController {

    public final DeviceService deviceService;
    public final DeviceMaintenanceService deviceMaintenanceService;
    public final DeviceFileUploadService deviceFileUploadService;
    public final DeviceCommentService deviceCommentService;
    public final DeviceSpecificationService deviceSpecificationService;
    public final DeviceSummaryService deviceSummaryService;

    @GetMapping("/client/{clientId}")
    public List<DeviceDTO> getDevicesByClientId(@PathVariable Integer clientId) {
        return deviceService.getDevicesByClientId(clientId);
    }

    @GetMapping("/all")
    public List<DeviceDTO> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/{deviceId}")
    public DeviceDTO getDeviceById(@PathVariable Integer deviceId) {
        return deviceService.getDeviceById(deviceId);
    }

    @GetMapping("/maintenances/{deviceId}")
    public List<MaintenanceDTO> getMaintenances(@PathVariable Integer deviceId) {
        return deviceMaintenanceService.getDeviceMaintenances(deviceId);
    }

    @GetMapping("/search")
    public List<DeviceDTO> searchAndFilterDevices(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "classificatorId", required = false) Integer classificatorId,
            @RequestParam(value = "clientId", required = false) Integer clientId,
            @RequestParam(value = "locationId", required = false) Integer locationId,
            @RequestParam(value = "writtenOff", required = false) Boolean writtenOff) {
        return deviceSpecificationService.searchAndFilterDevices(query, classificatorId, clientId, locationId, writtenOff);
    }

    @GetMapping("/comment/{deviceId}")
    public List<CommentDTO> getDeviceComments(@PathVariable Integer deviceId) {
        return deviceCommentService.getDeviceComments(deviceId);
    }

    @GetMapping("/files/{deviceId}")
    public List<FileUploadDTO> getDeviceFiles(@PathVariable Integer deviceId) {
        return deviceFileUploadService.getDeviceFiles(deviceId);
    }

    @GetMapping("/summary")
    public Map<String, Integer> getDevicesSummary(@RequestParam List<Integer> deviceIds) {
        return deviceSummaryService.getDevicesSummary(deviceIds);
    }

    @GetMapping("/client/summary/{clientId}")
    public Map<String, Integer> getClientDevicesSummary(@PathVariable Integer clientId) {
        return deviceSummaryService.getClientDevicesSummary(clientId);
    }

    @GetMapping("/history/{deviceId}")
    public List<DeviceDTO> getDeviceHistory(@PathVariable Integer deviceId) {
        return deviceService.getDeviceHistory(deviceId);
    }
}
