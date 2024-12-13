package com.demo.bait.service.PredefinedDeviceNameService;

import com.demo.bait.dto.PredefinedDeviceNameDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.PredefinedDeviceName;
import com.demo.bait.mapper.PredefinedDeviceNameMapper;
import com.demo.bait.repository.PredefinedDeviceNameRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PredefinedDeviceNameService {

    private PredefinedDeviceNameRepo predefinedDeviceNameRepo;
    private PredefinedDeviceNameMapper predefinedDeviceNameMapper;

    @Transactional
    public ResponseDTO addPredefinedDeviceName(String deviceName) {
        log.info("Adding a new predefined device name: '{}'", deviceName);
        try {
            PredefinedDeviceName predefinedDeviceName = new PredefinedDeviceName();
            predefinedDeviceName.setName(deviceName);
            predefinedDeviceNameRepo.save(predefinedDeviceName);
            log.info("Predefined device name '{}' added successfully", deviceName);
            return new ResponseDTO("New predefined device name added");
        } catch (Exception e) {
            log.error("Error while adding predefined device name: '{}'", deviceName, e);
            throw e;
        }
    }

    public List<PredefinedDeviceNameDTO> getPredefinedDeviceNames() {
        log.info("Fetching all predefined device names");
        try {
            List<PredefinedDeviceNameDTO> predefinedDeviceNames = predefinedDeviceNameMapper.toDtoList(predefinedDeviceNameRepo.findAll());
            log.info("Fetched {} predefined device names", predefinedDeviceNames.size());
            return predefinedDeviceNames;
        } catch (Exception e) {
            log.error("Error while fetching predefined device names", e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO removePredefinedDeviceName(Integer nameId) {
        log.info("Removing predefined device name with ID: {}", nameId);
        try {
            predefinedDeviceNameRepo.deleteById(nameId);
            log.info("Predefined device name with ID {} removed successfully", nameId);
            return new ResponseDTO("Predefined device name deleted");
        } catch (Exception e) {
            log.error("Error while removing predefined device name with ID: {}", nameId, e);
            throw e;
        }
    }
}
