package com.demo.bait.controller.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.DeviceClassificatorDTO;
import com.demo.bait.entity.classificator.DeviceClassificator;
import com.demo.bait.service.classificator.DeviceClassificatorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/device/classificator")
public class DeviceClassificatorController {

    public final DeviceClassificatorService deviceClassificatorService;

    @PostMapping("/add")
    public DeviceClassificatorDTO addDeviceClassificator(@RequestBody DeviceClassificatorDTO deviceClassificatorDTO) {
        return deviceClassificatorService.addDeviceClassificator(deviceClassificatorDTO);
    }

    @GetMapping("/all")
    public List<DeviceClassificatorDTO> getAllDeviceClassificators() {
        return deviceClassificatorService.getAllClassificators();
    }

    @GetMapping("/{deviceClassificatorId}")
    public DeviceClassificatorDTO getDeviceClassificatorById(@PathVariable Integer deviceClassificatorId) {
        return deviceClassificatorService.getDeviceClassificatorById(deviceClassificatorId);
    }

    @PutMapping("/update/{deviceClassificatorId}")
    public ResponseDTO updateDeviceClassificator(@PathVariable Integer deviceClassificatorId,
                                                 @RequestBody DeviceClassificatorDTO deviceClassificatorDTO) {
        return deviceClassificatorService.updateDeviceClassificator(deviceClassificatorId, deviceClassificatorDTO);
    }

    @DeleteMapping("/{deviceClassificatorId}")
    public ResponseDTO deleteDeviceClassificator(@PathVariable Integer deviceClassificatorId) {
        return deviceClassificatorService.deleteDeviceClassificator(deviceClassificatorId);
    }

    @GetMapping("/history/{deviceClassificatorId}")
    public List<DeviceClassificatorDTO> getDeviceClassificatorHistory(@PathVariable Integer deviceClassificatorId) {
        return deviceClassificatorService.getDeviceClassificatorHistory(deviceClassificatorId);
    }

    @GetMapping("/deleted")
    public List<DeviceClassificatorDTO> getDeletedDeviceClassificators() {
        return deviceClassificatorService.getDeletedDeviceClassificators();
    }
}
