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
        log.info("Adding attribute '{}' with value '{}' to all devices.", attributeName, attributeValue);
        try {
            List<Device> devices = deviceRepo.findAll();
            log.debug("Found {} devices to update.", devices.size());

            for (Device device : devices) {
                log.debug("Adding attribute '{}' to device with ID: {}", attributeName, device.getId());
                device.getAttributes().put(attributeName, attributeValue);
            }

            deviceRepo.saveAll(devices);
            log.info("Successfully added attribute '{}' to all devices.", attributeName);
        } catch (Exception e) {
            log.error("Error while adding attribute '{}' to all devices.", attributeName, e);
            throw e;
        }
    }

    @Transactional
    public DeviceDTO updateDeviceAttributes(Integer deviceId, Map<String, Object> newAttributes) {
        log.info("Updating attributes for device with ID: {}", deviceId);
        try {
            Device device = deviceRepo.findById(deviceId)
                    .orElseThrow(() -> {
                        log.warn("Device with ID {} not found.", deviceId);
                        return new EntityNotFoundException("Device not found");
                    });

            if (newAttributes != null) {
                log.debug("Adding new attributes: {} to device with ID: {}", newAttributes, deviceId);
                device.getAttributes().putAll(newAttributes);
            }

            Device updatedDevice = deviceRepo.save(device);
            log.info("Successfully updated attributes for device with ID: {}", deviceId);
            return deviceMapper.toDto(updatedDevice);
        } catch (Exception e) {
            log.error("Error while updating attributes for device with ID: {}", deviceId, e);
            throw e;
        }
    }

    @Transactional
    public DeviceDTO removeDeviceAttribute(Integer deviceId, String attributeName) {
        log.info("Removing attribute '{}' from device with ID: {}", attributeName, deviceId);
        try {
            Device device = deviceRepo.findById(deviceId)
                    .orElseThrow(() -> {
                        log.warn("Device with ID {} not found.", deviceId);
                        return new EntityNotFoundException("Device not found");
                    });

            if (device.getAttributes().containsKey(attributeName)) {
                log.debug("Removing attribute '{}' from device with ID: {}", attributeName, deviceId);
                device.getAttributes().remove(attributeName);
            } else {
                log.warn("Attribute '{}' not found in device with ID: {}", attributeName, deviceId);
            }

            Device updatedDevice = deviceRepo.save(device);
            log.info("Successfully removed attribute '{}' from device with ID: {}", attributeName, deviceId);
            return deviceMapper.toDto(updatedDevice);
        } catch (Exception e) {
            log.error("Error while removing attribute '{}' from device with ID: {}", attributeName, deviceId, e);
            throw e;
        }
    }
}
