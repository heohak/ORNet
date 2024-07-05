package com.demo.bait.controller;

import com.demo.bait.dto.LinkedDeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.LinkedDeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/linked/device")
public class LinkedDeviceController {

    public final LinkedDeviceService linkedDeviceService;

    @PostMapping("/add")
    public ResponseDTO addLinkedDevice(@RequestBody LinkedDeviceDTO linkedDeviceDTO) {
        return linkedDeviceService.addLinkedDevice(linkedDeviceDTO);
    }

    @GetMapping("/all")
    public List<LinkedDeviceDTO> getAllLinkedDevices() {
        return linkedDeviceService.getAllLinkedDevices();
    }

    @GetMapping("/{deviceId}")
    public List<LinkedDeviceDTO> getLinkedDevicesByDeviceId(@PathVariable Integer deviceId) {
        return linkedDeviceService.getLinkedDevicesByDeviceId(deviceId);
    }

    @PutMapping("/link/{linkedDeviceId}/{deviceId}")
    public ResponseDTO linkDevice(@PathVariable Integer linkedDeviceId, @PathVariable Integer deviceId) {
        return linkedDeviceService.linkDevice(linkedDeviceId, deviceId);
    }

    @DeleteMapping("/{deviceId}")
    public ResponseDTO deleteDevice(@PathVariable Integer deviceId) {
        return linkedDeviceService.deleteLinkedDevice(deviceId);
    }
}
