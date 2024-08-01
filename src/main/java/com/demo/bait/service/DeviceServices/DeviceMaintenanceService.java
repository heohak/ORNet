package com.demo.bait.service.DeviceServices;

import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.repository.DeviceRepo;
import com.demo.bait.repository.MaintenanceRepo;
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
public class DeviceMaintenanceService {

    private DeviceRepo deviceRepo;
    private MaintenanceRepo maintenanceRepo;
    private MaintenanceMapper maintenanceMapper;

    @Transactional
    public ResponseDTO addMaintenanceToDevice(Integer deviceId, Integer maintenanceId) {
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
        Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);

        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }
        if (maintenanceOpt.isEmpty()) {
            throw new EntityNotFoundException("Maintenance with id " + maintenanceId + " not found");
        }

        Device device = deviceOpt.get();
        Maintenance maintenance = maintenanceOpt.get();
        device.getMaintenances().add(maintenance);
        deviceRepo.save(device);
        return new ResponseDTO("Maintenance added successfully to device");
    }

    public List<MaintenanceDTO> getDeviceMaintenances(Integer deviceId) {
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }

        Device device = deviceOpt.get();
        return maintenanceMapper.toDtoList(device.getMaintenances().stream().toList());
    }
}
