package com.demo.bait.service.DeviceServices;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.repository.DeviceRepo;
import com.demo.bait.repository.MaintenanceRepo;
import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceMaintenanceService {

    private DeviceRepo deviceRepo;
    private MaintenanceRepo maintenanceRepo;
    private MaintenanceMapper maintenanceMapper;
    private MaintenanceService maintenanceService;

    @Transactional
    public ResponseDTO addMaintenanceToDevice(Integer deviceId, Integer maintenanceId) {
        log.info("Adding maintenance with ID: {} to device with ID: {}", maintenanceId, deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);

            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }
            if (maintenanceOpt.isEmpty()) {
                log.warn("Maintenance with ID {} not found.", maintenanceId);
                throw new EntityNotFoundException("Maintenance with id " + maintenanceId + " not found");
            }

            Device device = deviceOpt.get();
            Maintenance maintenance = maintenanceOpt.get();
            log.debug("Adding maintenance with ID: {} to device with ID: {}", maintenanceId, deviceId);
            device.getMaintenances().add(maintenance);
            deviceRepo.save(device);

            log.info("Successfully added maintenance with ID: {} to device with ID: {}", maintenanceId, deviceId);
            return new ResponseDTO("Maintenance added successfully to device");
        } catch (Exception e) {
            log.error("Error while adding maintenance with ID: {} to device with ID: {}", maintenanceId, deviceId, e);
            throw e;
        }
    }

    public List<MaintenanceDTO> getDeviceMaintenances(Integer deviceId) {
        if (deviceId == null) {
            log.warn("Device ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        log.info("Fetching maintenances for device with ID: {}", deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }

            Device device = deviceOpt.get();
            List<MaintenanceDTO> maintenances = maintenanceMapper.toDtoList(device.getMaintenances().stream().toList());
            log.info("Fetched {} maintenances for device with ID: {}", maintenances.size(), deviceId);
            return maintenances;
        } catch (Exception e) {
            log.error("Error while fetching maintenances for device with ID: {}", deviceId, e);
            throw e;
        }
    }

    public void updateMaintenances(Device device, DeviceDTO deviceDTO) {
        log.info("Updating maintenances for device with ID: {}", device.getId());
        try {
            if (deviceDTO.maintenanceIds() != null) {
                log.debug("Updating maintenances with IDs: {} for device with ID: {}", deviceDTO.maintenanceIds(), device.getId());
                Set<Maintenance> maintenances = maintenanceService.maintenanceIdsToMaintenancesSet(deviceDTO.maintenanceIds());
                device.setMaintenances(maintenances);
                log.info("Successfully updated maintenances for device with ID: {}", device.getId());
            } else {
                log.debug("No maintenances provided to update for device with ID: {}", device.getId());
            }
        } catch (Exception e) {
            log.error("Error while updating maintenances for device with ID: {}", device.getId(), e);
            throw e;
        }
    }
}
