package com.demo.bait.controller.PredefinedDeviceNameController;

import com.demo.bait.dto.PredefinedDeviceNameDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.PredefinedDeviceNameService.PredefinedDeviceNameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/predefined")
public class PredefinedDeviceNameController {

    private PredefinedDeviceNameService predefinedDeviceNameService;

    @GetMapping("/names")
    public List<PredefinedDeviceNameDTO> getAllPredefinedDeviceNames() {
        return predefinedDeviceNameService.getPredefinedDeviceNames();
    }

    @PostMapping("/add")
    public ResponseDTO addPredefinedDeviceName(@RequestParam String deviceName) {
        return predefinedDeviceNameService.addPredefinedDeviceName(deviceName);
    }

    @DeleteMapping("/delete/{nameId}")
    public ResponseDTO deletePredefinedDeviceName(@PathVariable Integer nameId) {
        return predefinedDeviceNameService.removePredefinedDeviceName(nameId);
    }
}
