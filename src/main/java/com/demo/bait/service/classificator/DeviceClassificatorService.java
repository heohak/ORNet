package com.demo.bait.service.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.DeviceClassificatorDTO;
import com.demo.bait.entity.classificator.DeviceClassificator;
import com.demo.bait.mapper.classificator.DeviceClassificatorMapper;
import com.demo.bait.repository.classificator.DeviceClassificatorRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceClassificatorService {

    private DeviceClassificatorRepo deviceClassificatorRepo;
    private DeviceClassificatorMapper deviceClassificatorMapper;

    @Transactional
    public ResponseDTO addDeviceClassificator(DeviceClassificatorDTO deviceClassificatorDTO) {
        DeviceClassificator deviceClassificator = new DeviceClassificator();
        deviceClassificator.setName(deviceClassificatorDTO.name());
        deviceClassificatorRepo.save(deviceClassificator);
        return new ResponseDTO("Device classificator added successfully");
    }

    @Transactional
    public ResponseDTO updateDeviceClassificator(Integer deviceClassificatorId,
                                                 DeviceClassificatorDTO deviceClassificatorDTO) {
        Optional<DeviceClassificator> deviceClassificatorOpt = deviceClassificatorRepo.findById(deviceClassificatorId);
        if (deviceClassificatorOpt.isEmpty()) {
            throw new EntityNotFoundException("Device classificator with id " + deviceClassificatorId + " not found");
        }
        DeviceClassificator classificator = deviceClassificatorOpt.get();
        if (deviceClassificatorDTO.name() != null) {
            classificator.setName(deviceClassificatorDTO.name());
        }
        deviceClassificatorRepo.save(classificator);
        return new ResponseDTO("Device classificator updated successfully");
    }

    public List<DeviceClassificatorDTO> getAllClassificators() {
        return deviceClassificatorMapper.toDtoList(deviceClassificatorRepo.findAll());
    }

    @Transactional
    public ResponseDTO deleteDeviceClassificator(Integer deviceClassificatorId) {
        deviceClassificatorRepo.deleteById(deviceClassificatorId);
        return new ResponseDTO("Device classificator deleted successfully");
    }
}
