package com.demo.bait.controller;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class DeviceController {

    public final DeviceService deviceService;

    @PostMapping("/device")
    public ResponseDTO addDevice(DeviceDTO deviceDTO) {
        return deviceService.addDevice(deviceDTO);
    }

    @GetMapping("/devices/{clientId}")
    public List<DeviceDTO> getDevicesByClientId(@PathVariable Integer clientId) {
        return deviceService.getDevicesByClientId(clientId);
    }
}
