package com.demo.bait.service.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.DeviceClassificatorDTO;
import com.demo.bait.entity.classificator.DeviceClassificator;
import com.demo.bait.mapper.classificator.DeviceClassificatorMapper;
import com.demo.bait.repository.classificator.DeviceClassificatorRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceClassificatorService {

    private DeviceClassificatorRepo deviceClassificatorRepo;
    private DeviceClassificatorMapper deviceClassificatorMapper;

    public ResponseDTO addDeviceClassificator(DeviceClassificatorDTO deviceClassificatorDTO) {
        DeviceClassificator deviceClassificator = new DeviceClassificator();
        deviceClassificator.setName(deviceClassificatorDTO.name());
        deviceClassificatorRepo.save(deviceClassificator);
        return new ResponseDTO("Device classificator added successfully");
    }

    public List<DeviceClassificatorDTO> getAllClassificators() {
        return deviceClassificatorMapper.toDtoList(deviceClassificatorRepo.findAll());
    }
}
