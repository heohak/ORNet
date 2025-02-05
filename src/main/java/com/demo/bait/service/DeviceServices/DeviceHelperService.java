package com.demo.bait.service.DeviceServices;

import com.demo.bait.entity.Device;
import com.demo.bait.repository.DeviceRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceHelperService {

    private DeviceRepo deviceRepo;

    public Set<Device> deviceIdsToDevicesSet(List<Integer> deviceIds) {
        log.info("Converting device IDs to Device set: {}", deviceIds);
        try {
            Set<Device> devices = new HashSet<>();
            for (Integer deviceId : deviceIds) {
                Device device = deviceRepo.findById(deviceId)
                        .orElseThrow(() -> {
                            log.warn("Invalid device ID: {}", deviceId);
                            return new IllegalArgumentException("Invalid device ID: " + deviceId);
                        });
                devices.add(device);
            }
            log.info("Converted {} device IDs to Device set.", devices.size());
            return devices;
        } catch (Exception e) {
            log.error("Error while converting device IDs to Device set: {}", deviceIds, e);
            throw e;
        }
    }
}
