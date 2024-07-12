package com.demo.bait.controller.classificatro;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.DeviceClassificatorDTO;
import com.demo.bait.service.classificator.DeviceClassificatorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/device/classificator")
public class DeviceClassificatorController {

    public final DeviceClassificatorService deviceClassificatorService;

    @PostMapping("/add")
    public ResponseDTO addDeviceClassificator(@RequestBody DeviceClassificatorDTO deviceClassificatorDTO) {
        return deviceClassificatorService.addDeviceClassificator(deviceClassificatorDTO);
    }

    @GetMapping("/all")
    public List<DeviceClassificatorDTO> getAllDeviceClassificators() {
        return deviceClassificatorService.getAllClassificators();
    }
}
