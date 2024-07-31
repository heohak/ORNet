package com.demo.bait.controller.SoftwareController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.SoftwareServices.SoftwareService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/software")
public class SoftwarePutController {

    public final SoftwareService softwareService;

    @PutMapping("/add/client/{softwareId}/{clientId}")
    public ResponseDTO addClientToSoftware(@PathVariable Integer softwareId, @PathVariable Integer clientId) {
        return softwareService.addClientToSoftware(softwareId, clientId);
    }
}
