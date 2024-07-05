package com.demo.bait.controller;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.SoftwareDTO;
import com.demo.bait.service.SoftwareService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/software")
public class SoftwareController {

    public final SoftwareService softwareService;

    @PostMapping("/add")
    public ResponseDTO addSoftware(@RequestBody SoftwareDTO softwareDTO) {
        return softwareService.addSoftware(softwareDTO);
    }

    @GetMapping("/all")
    public List<SoftwareDTO> getAllSoftwareVariations() {
        return softwareService.getAllSoftwareVariations();
    }

    @PutMapping("/add/client/{softwareId}/{clientId}")
    public ResponseDTO addClientToSoftware(@PathVariable Integer softwareId, @PathVariable Integer clientId) {
        return softwareService.addClientToSoftware(softwareId, clientId);
    }

    @GetMapping("/{clientId}")
    public List<SoftwareDTO> getSoftwareByClientId(@PathVariable Integer clientId) {
        return softwareService.getSoftwareByClientId(clientId);
    }

    @DeleteMapping("/{softwareId}")
    public ResponseDTO deleteSoftware(@PathVariable Integer softwareId) {
        return softwareService.deleteSoftware(softwareId);
    }
}
