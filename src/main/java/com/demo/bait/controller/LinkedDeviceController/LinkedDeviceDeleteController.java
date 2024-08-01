package com.demo.bait.controller.LinkedDeviceController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.LinkedDeviceServices.LinkedDeviceAttributeService;
import com.demo.bait.service.LinkedDeviceServices.LinkedDeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/linked/device")
public class LinkedDeviceDeleteController {

    public final LinkedDeviceService linkedDeviceService;
    public final LinkedDeviceAttributeService linkedDeviceAttributeService;

    @DeleteMapping("/{deviceId}")
    public ResponseDTO deleteDevice(@PathVariable Integer deviceId) {
        return linkedDeviceService.deleteLinkedDevice(deviceId);
    }

    @DeleteMapping("/{linkedDeviceId}/{attributeName}")
    public ResponseDTO removeLinkedDeviceAttribute(@PathVariable Integer linkedDeviceId,
                                                   @PathVariable String attributeName) {
        return linkedDeviceAttributeService.removeLinkedDeviceAttribute(linkedDeviceId, attributeName);
    }
}
