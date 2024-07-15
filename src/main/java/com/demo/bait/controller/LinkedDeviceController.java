package com.demo.bait.controller;

import com.demo.bait.dto.LinkedDeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.LinkedDeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/{linkedDeviceId}/attributes")
    public ResponseDTO updateLinkedDeviceAttributes(@PathVariable Integer linkedDeviceId,
                                                    @RequestBody Map<String, Object> attributes) {
        return linkedDeviceService.updateLinkedDeviceAttributes(linkedDeviceId, attributes);
    }

    @DeleteMapping("/{linkedDeviceId}/{attributeName}")
    public ResponseDTO removeLinkedDeviceAttribute(@PathVariable Integer linkedDeviceId,
                                                   @PathVariable String attributeName) {
        return linkedDeviceService.removeLinkedDeviceAttribute(linkedDeviceId, attributeName);
    }
}
