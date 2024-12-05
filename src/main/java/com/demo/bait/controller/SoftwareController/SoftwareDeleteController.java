package com.demo.bait.controller.SoftwareController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.SoftwareServices.SoftwareService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/software")
public class SoftwareDeleteController {

    public final SoftwareService softwareService;

    @DeleteMapping("/{softwareId}")
    public ResponseDTO deleteSoftware(@PathVariable Integer softwareId) {
        return softwareService.deleteSoftware(softwareId);
    }
}
