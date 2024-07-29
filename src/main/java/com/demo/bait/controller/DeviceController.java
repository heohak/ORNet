package com.demo.bait.controller;

import com.demo.bait.dto.*;
import com.demo.bait.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/device")
public class DeviceController {

    public final DeviceService deviceService;

    @PostMapping("/add")
    public ResponseDTO addDevice(@RequestBody DeviceDTO deviceDTO) {
        return deviceService.addDevice(deviceDTO);
    }

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

    @DeleteMapping("/delete/{deviceId}")
    public ResponseDTO deleteDevice(@PathVariable Integer deviceId) {
        return deviceService.deleteDevice(deviceId);
    }

    @PutMapping("/maintenance/{deviceId}/{maintenanceId}")
    public ResponseDTO addMaintenance(@PathVariable Integer deviceId, @PathVariable Integer maintenanceId) {
        return deviceService.addMaintenanceToDevice(deviceId, maintenanceId);
    }

    @PutMapping("/upload/{deviceId}")
    public ResponseDTO uploadFiles(@PathVariable Integer deviceId, @RequestParam("files") List<MultipartFile> files)
            throws IOException {
        return deviceService.uploadFilesToDevice(deviceId, files);
    }

    @PutMapping("/location/{deviceId}/{locationId}")
    public ResponseDTO addLocation(@PathVariable Integer deviceId, @PathVariable Integer locationId) {
        return deviceService.addLocationToDevice(deviceId, locationId);
    }

    @GetMapping("/maintenances/{deviceId}")
    public List<MaintenanceDTO> getMaintenances(@PathVariable Integer deviceId) {
        return deviceService.getDeviceMaintenances(deviceId);
    }

    @PutMapping("/client/{deviceId}/{clientId}")
    public ResponseDTO addClient(@PathVariable Integer deviceId, @PathVariable Integer clientId) {
        return deviceService.addClientToDevice(deviceId, clientId);
    }

    @PutMapping("/{deviceId}/attributes")
    public DeviceDTO updateDeviceAttributes(@PathVariable Integer deviceId, @RequestBody Map<String, Object> attributes) {
        return deviceService.updateDeviceAttributes(deviceId, attributes);
    }

    @DeleteMapping("/{deviceId}/{attributeName}")
    public DeviceDTO removeDeviceAttribute(@PathVariable Integer deviceId, @PathVariable String attributeName) {
        return deviceService.removeDeviceAttribute(deviceId, attributeName);
    }

    @PostMapping("/attributes/add-to-all")
    public void addAttributeToAllDevices(@RequestBody Map<String, Object> attribute) {
        if (attribute.size() != 1) {
            throw new IllegalArgumentException("Please provide exactly one attribute name-value pair");
        }
        String attributeName = attribute.keySet().iterator().next();
        Object attributeValue = attribute.values().iterator().next();
        deviceService.addAttributeToAllDevices(attributeName, attributeValue);
    }

    @PutMapping("/classificator/{deviceId}/{classificatorId}")
    public ResponseDTO addClassificatorToDevice(@PathVariable Integer deviceId, @PathVariable Integer classificatorId) {
        return deviceService.addClassificatorToDevice(deviceId, classificatorId);
    }

    // written off date PUT endpoint
    @PutMapping("/written-off/{deviceId}")
    public ResponseDTO addWrittenOffDate(@PathVariable Integer deviceId, @RequestBody DeviceDTO deviceDTO) {
        return deviceService.addWrittenOffDate(deviceId, deviceDTO);
    }


//    @GetMapping("/classificator/{classificatorId}")
//    public List<DeviceDTO> getDevicesByClassificatorId(@PathVariable Integer classificatorId) {
//        return deviceService.getDevicesByClassificatorId(classificatorId);
//    }
//
//    @GetMapping("/search")
//    public List<DeviceDTO> searchDevices(@RequestParam("q") String query) {
//        return deviceService.searchDevices(query);
//    }

    @GetMapping("/search")
    public List<DeviceDTO> searchAndFilterDevices(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "classificatorId", required = false) Integer classificatorId,
            @RequestParam(value = "clientId", required = false) Integer clientId) {
        return deviceService.searchAndFilterDevices(query, classificatorId, clientId);
    }

//    @GetMapping("/filter/{clientId}/{classificatorId}")
//    public List<DeviceDTO> filterDevicesByClientAndClassificator(@PathVariable Integer clientId,
//                                                                 @PathVariable Integer classificatorId) {
//        return deviceService.getDevicesByClientIdAndClassificatorId(clientId, classificatorId);
//    }

    @PutMapping("/comment/{deviceId}")
    public ResponseDTO addCommentToDevice(@PathVariable Integer deviceId, @RequestParam("comment") String comment) {
        return deviceService.addCommentToDevice(deviceId, comment);
    }

    @GetMapping("/comment/{deviceId}")
    public List<CommentDTO> getDeviceComments(@PathVariable Integer deviceId) {
        return deviceService.getDeviceComments(deviceId);
    }

    @GetMapping("/files/{deviceId}")
    public List<FileUploadDTO> getDeviceFiles(@PathVariable Integer deviceId) {
        return deviceService.getDeviceFiles(deviceId);
    }
}
