package com.demo.bait.controller.SoftwareController;

import com.demo.bait.service.SoftwareServices.SoftwareService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/software")
public class SoftwareController {

    public final SoftwareService softwareService;
}
