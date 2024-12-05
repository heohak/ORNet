package com.demo.bait.controller.DeviceController;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.DeviceServices.DeviceAttributeService;
import com.demo.bait.service.DeviceServices.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/device")
public class DeviceDeleteController {

    public final DeviceService deviceService;
    public final DeviceAttributeService deviceAttributeService;

    @DeleteMapping("/delete/{deviceId}")
    public ResponseDTO deleteDevice(@PathVariable Integer deviceId) {
        return deviceService.deleteDevice(deviceId);
    }

    @DeleteMapping("/{deviceId}/{attributeName}")
    public DeviceDTO removeDeviceAttribute(@PathVariable Integer deviceId, @PathVariable String attributeName) {
        return deviceAttributeService.removeDeviceAttribute(deviceId, attributeName);
    }
}
