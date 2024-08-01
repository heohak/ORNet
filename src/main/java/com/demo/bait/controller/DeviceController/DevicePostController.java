package com.demo.bait.controller.DeviceController;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.DeviceServices.DeviceAttributeService;
import com.demo.bait.service.DeviceServices.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/device")
public class DevicePostController {

    public final DeviceService deviceService;
    public final DeviceAttributeService deviceAttributeService;

    @PostMapping("/add")
    public ResponseDTO addDevice(@RequestBody DeviceDTO deviceDTO) {
        return deviceService.addDevice(deviceDTO);
    }

    @PostMapping("/attributes/add-to-all")
    public void addAttributeToAllDevices(@RequestBody Map<String, Object> attribute) {
        if (attribute.size() != 1) {
            throw new IllegalArgumentException("Please provide exactly one attribute name-value pair");
        }
        String attributeName = attribute.keySet().iterator().next();
        Object attributeValue = attribute.values().iterator().next();
        deviceAttributeService.addAttributeToAllDevices(attributeName, attributeValue);
    }
}
