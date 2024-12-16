package com.demo.bait.controller.DeviceController;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.DeviceServices.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/device")
public class DevicePutController {

    public final DeviceService deviceService;
    public final DeviceMaintenanceService deviceMaintenanceService;
    public final DeviceFileUploadService deviceFileUploadService;
    public final DeviceCommentService deviceCommentService;
    public final DeviceAttributeService deviceAttributeService;

    @PutMapping("/maintenance/{deviceId}/{maintenanceId}")
    public ResponseDTO addMaintenance(@PathVariable Integer deviceId, @PathVariable Integer maintenanceId) {
        return deviceMaintenanceService.addMaintenanceToDevice(deviceId, maintenanceId);
    }

    @PutMapping("/upload/{deviceId}")
    public ResponseDTO uploadFiles(@PathVariable Integer deviceId, @RequestParam("files") List<MultipartFile> files)
            throws IOException {
        return deviceFileUploadService.uploadFilesToDevice(deviceId, files);
    }

    @PutMapping("/location/{deviceId}/{locationId}")
    public ResponseDTO addLocation(@PathVariable Integer deviceId, @PathVariable Integer locationId) {
        return deviceService.addLocationToDevice(deviceId, locationId);
    }

    @PutMapping("/client/{deviceId}/{clientId}")
    public ResponseDTO addClient(@PathVariable Integer deviceId, @PathVariable Integer clientId) {
        return deviceService.addClientToDevice(deviceId, clientId);
    }

    @PutMapping("/{deviceId}/attributes")
    public DeviceDTO updateDeviceAttributes(@PathVariable Integer deviceId, @RequestBody Map<String, Object> attributes) {
        return deviceAttributeService.updateDeviceAttributes(deviceId, attributes);
    }

    @PutMapping("/classificator/{deviceId}/{classificatorId}")
    public ResponseDTO addClassificatorToDevice(@PathVariable Integer deviceId, @PathVariable Integer classificatorId) {
        return deviceService.addClassificatorToDevice(deviceId, classificatorId);
    }

    @PutMapping("/written-off/{deviceId}")
    public ResponseDTO addWrittenOffDate(@PathVariable Integer deviceId,
                                         @RequestParam("writtenOffDate") LocalDate writtenOffDate,
                                         @RequestBody(required = false) String comment) {
        return deviceService.addWrittenOffDate(deviceId, writtenOffDate, comment);
    }

    @PutMapping("/reactivate/{deviceId}")
    public ResponseDTO reactivateDevice(@PathVariable Integer deviceId,
                                        @RequestBody(required = false) String comment) {
        return deviceService.reactivateDevice(deviceId, comment);
    }

    @PutMapping("/comment/{deviceId}")
    public ResponseDTO addCommentToDevice(@PathVariable Integer deviceId, @RequestBody String comment) {
        return deviceCommentService.addCommentToDevice(deviceId, comment);
    }

    @PutMapping("/update/{deviceId}")
    public ResponseDTO updateDevice(@PathVariable Integer deviceId, @RequestBody DeviceDTO deviceDTO) {
        return deviceService.updateDevice(deviceId, deviceDTO);
    }
}
