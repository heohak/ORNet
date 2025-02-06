package com.demo.bait.controller.DeviceController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.*;
import com.demo.bait.service.DeviceServices.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/device")
public class DeviceGetController {

    public final DeviceService deviceService;
//    public final DeviceMaintenanceService deviceMaintenanceService;
    public final DeviceFileUploadService deviceFileUploadService;
    public final DeviceCommentService deviceCommentService;
    public final DeviceSpecificationService deviceSpecificationService;
    public final DeviceSummaryService deviceSummaryService;
    private RequestParamParser requestParamParser;


    @GetMapping("/client/{clientId}")
    public List<DeviceDTO> getDevicesByClientId(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "clientId");
        return deviceService.getDevicesByClientId(parsedClientId);
    }

    @GetMapping("/all")
    public List<DeviceDTO> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/{deviceId}")
    public DeviceDTO getDeviceById(@PathVariable String deviceId) {
        Integer parsedDeviceId = requestParamParser.parseId(deviceId, "deviceId");
        return deviceService.getDeviceById(parsedDeviceId);
    }

//    @GetMapping("/maintenances/{deviceId}")
//    public List<MaintenanceDTO> getMaintenances(@PathVariable String deviceId) {
//        Integer parsedDeviceId = requestParamParser.parseId(deviceId, "deviceId");
//        return deviceMaintenanceService.getDeviceMaintenances(parsedDeviceId);
//    }

    @GetMapping("/maintenances/{deviceId}")
    public List<MaintenanceDTO> getMaintenances(@PathVariable String deviceId) {
        Integer parsedDeviceId = requestParamParser.parseId(deviceId, "deviceId");
        return deviceService.getDeviceMaintenances(parsedDeviceId);
    }

    @GetMapping("/search")
    public List<DeviceDTO> searchAndFilterDevices(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "classificatorId", required = false) Integer classificatorId,
            @RequestParam(value = "clientId", required = false) Integer clientId,
            @RequestParam(value = "locationId", required = false) Integer locationId,
            @RequestParam(value = "writtenOff", required = false) Boolean writtenOff,
            @RequestParam(value = "customerRegisterNos", required = false) String customerRegisterNos) {
        return deviceSpecificationService.searchAndFilterDevices(query, classificatorId, clientId, locationId,
                writtenOff, customerRegisterNos);
    }

    @GetMapping("/comment/{deviceId}")
    public List<CommentDTO> getDeviceComments(@PathVariable String deviceId) {
        Integer parsedDeviceId = requestParamParser.parseId(deviceId, "deviceId");
        return deviceCommentService.getDeviceComments(parsedDeviceId);
    }

    @GetMapping("/files/{deviceId}")
    public List<FileUploadDTO> getDeviceFiles(@PathVariable String deviceId) {
        Integer parsedDeviceId = requestParamParser.parseId(deviceId, "deviceId");
        return deviceFileUploadService.getDeviceFiles(parsedDeviceId);
    }

    @GetMapping("/summary")
    public Map<String, Integer> getDevicesSummary(@RequestParam List<Integer> deviceIds) {
        return deviceSummaryService.getDevicesSummary(deviceIds);
    }

    @GetMapping("/client/summary/{clientId}")
    public Map<String, Integer> getClientDevicesSummary(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "clientId");
        return deviceSummaryService.getClientDevicesSummary(parsedClientId);
    }

    @GetMapping("/history/{deviceId}")
    public List<DeviceDTO> getDeviceHistory(@PathVariable String deviceId) {
        Integer parsedDeviceId = requestParamParser.parseId(deviceId, "deviceId");
        return deviceService.getDeviceHistory(parsedDeviceId);
    }

    @GetMapping("/tickets/{deviceId}")
    public List<TicketDTO> getDeviceTickets(@PathVariable String deviceId) {
        Integer parsedDeviceId = requestParamParser.parseId(deviceId, "deviceId");
        return deviceService.getDeviceTickets(parsedDeviceId);
    }
}
