package com.demo.bait.controller.LinkedDeviceController;

import com.demo.bait.dto.LinkedDeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.LinkedDeviceServices.LinkedDeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/linked/device")
public class LinkedDevicePostController {

    public final LinkedDeviceService linkedDeviceService;

    @PostMapping("/add")
    public ResponseDTO addLinkedDevice(@RequestBody LinkedDeviceDTO linkedDeviceDTO) {
        return linkedDeviceService.addLinkedDevice(linkedDeviceDTO);
    }
}
