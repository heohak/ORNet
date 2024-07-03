package com.demo.bait.service;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.mapper.DeviceMapper;
import com.demo.bait.repository.DeviceRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceService {

    private DeviceRepo deviceRepo;
    private DeviceMapper deviceMapper;

    public ResponseDTO addDevice(DeviceDTO deviceDTO) {
        Device device = new Device();
        device.setClientId(deviceDTO.clientId());
        device.setDeviceName(deviceDTO.deviceName());
        device.setSerialNumber(deviceDTO.serialNumber());
        deviceRepo.save(device);
        return new ResponseDTO("Device added successfully");
    }

    public List<DeviceDTO> getDevicesByClientId(Integer clientId) {
        return deviceMapper.toDtoList(deviceRepo.findByClientId(clientId));
    }
}
