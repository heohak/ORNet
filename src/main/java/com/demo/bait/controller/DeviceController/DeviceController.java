package com.demo.bait.controller.DeviceController;

import com.demo.bait.service.DeviceServices.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/device")
public class DeviceController {

    public final DeviceService deviceService;
}
