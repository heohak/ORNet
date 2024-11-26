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
        PredefinedDeviceName predefinedDeviceName = new PredefinedDeviceName();
        predefinedDeviceName.setName(deviceName);
        predefinedDeviceNameRepo.save(predefinedDeviceName);
        return new ResponseDTO("New predefined device name added");
    }

    public List<PredefinedDeviceNameDTO> getPredefinedDeviceNames() {
        return predefinedDeviceNameMapper.toDtoList(predefinedDeviceNameRepo.findAll());
    }

    @Transactional
    public ResponseDTO removePredefinedDeviceName(Integer nameId) {
        predefinedDeviceNameRepo.deleteById(nameId);
        return new ResponseDTO("Predefined device name deleted");
    }
}
