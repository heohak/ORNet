package com.demo.bait.service.DeviceServices;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.mapper.DeviceMapper;
import com.demo.bait.repository.DeviceRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceAttributeService {

    private DeviceRepo deviceRepo;
    private DeviceMapper deviceMapper;

    @Transactional
    public void addAttributeToAllDevices(String attributeName, Object attributeValue) {
        List<Device> devices = deviceRepo.findAll();
        for (Device device : devices) {
            device.getAttributes().put(attributeName, attributeValue);
        }
        deviceRepo.saveAll(devices);
    }

    @Transactional
    public DeviceDTO updateDeviceAttributes(Integer deviceId, Map<String, Object> newAttributes) {
        Device device = deviceRepo.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found"));
        if (newAttributes != null) {
            device.getAttributes().putAll(newAttributes);
        }
        Device updatedDevice = deviceRepo.save(device);
        return deviceMapper.toDto(updatedDevice);
    }

    @Transactional
    public DeviceDTO removeDeviceAttribute(Integer deviceId, String attributeName) {
        Device device = deviceRepo.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found"));
        device.getAttributes().remove(attributeName);
        Device updatedDevice = deviceRepo.save(device);
        return deviceMapper.toDto(updatedDevice);
    }
}
