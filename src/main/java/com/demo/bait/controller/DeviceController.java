package com.demo.bait.controller;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
