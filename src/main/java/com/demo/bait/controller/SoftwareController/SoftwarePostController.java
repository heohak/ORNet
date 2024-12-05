package com.demo.bait.controller.SoftwareController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.SoftwareDTO;
import com.demo.bait.service.SoftwareServices.SoftwareService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/software")
public class SoftwarePostController {

    public final SoftwareService softwareService;

    @PostMapping("/add")
    public ResponseDTO addSoftware(@RequestBody SoftwareDTO softwareDTO) {
        return softwareService.addSoftware(softwareDTO);
    }
}
