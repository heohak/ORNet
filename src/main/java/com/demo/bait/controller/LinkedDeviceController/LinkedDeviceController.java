package com.demo.bait.controller.LinkedDeviceController;

import com.demo.bait.service.LinkedDeviceServices.LinkedDeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/linked/device")
public class LinkedDeviceController {

    public final LinkedDeviceService linkedDeviceService;
}
