package com.demo.bait.service.LocationServices;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.repository.LocationRepo;
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
public class LocationMaintenanceService {

    private LocationRepo locationRepo;
    private MaintenanceMapper maintenanceMapper;
    private MaintenanceService maintenanceService;
    private MaintenanceRepo maintenanceRepo;

    @Transactional
    public void updateLocationMaintenance(Location location, LocationDTO locationDTO) {
        log.info("Updating maintenances for location ID: {}", location.getId());
        try {
            if (locationDTO.maintenanceIds() != null) {
                log.debug("New maintenance IDs for location ID {}: {}", location.getId(), locationDTO.maintenanceIds());
                Set<Maintenance> maintenances = maintenanceService
                        .maintenanceIdsToMaintenancesSet(locationDTO.maintenanceIds());
                location.setMaintenances(maintenances);
                log.info("Maintenances updated successfully for location ID: {}", location.getId());
            } else {
                log.debug("No maintenance IDs provided for location ID: {}", location.getId());
            }
        } catch (Exception e) {
            log.error("Error while updating maintenances for location ID: {}", location.getId(), e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO addMaintenanceToLocation(Integer locationId, MaintenanceDTO maintenanceDTO) {
        log.info("Adding maintenance to location ID: {}", locationId);
        try {
            Optional<Location> locationOpt = locationRepo.findById(locationId);
            if (locationOpt.isEmpty()) {
                log.warn("Location with ID {} not found", locationId);
                throw new EntityNotFoundException("Location with id " + locationId + " not found");
            }

            Location location = locationOpt.get();
            Integer maintenanceId = Integer.parseInt(maintenanceService.addMaintenance(maintenanceDTO).token());
            log.debug("New maintenance added with ID: {}", maintenanceId);

            location.getMaintenances().add(maintenanceRepo.getReferenceById(maintenanceId));
            locationRepo.save(location);

            log.info("Maintenance with ID {} added successfully to location ID: {}", maintenanceId, locationId);
            return new ResponseDTO(maintenanceId.toString());
        } catch (Exception e) {
            log.error("Error while adding maintenance to location ID: {}", locationId, e);
            throw e;
        }
    }

    public List<MaintenanceDTO> getLocationMaintenances(Integer locationId) {
        if (locationId == null) {
            log.warn("Location ID is null. Returning empty list");
            return Collections.emptyList();
        }

        log.info("Fetching maintenances for location ID: {}", locationId);
        try {
            Optional<Location> locationOpt = locationRepo.findById(locationId);
            if (locationOpt.isEmpty()) {
                log.warn("Location with ID {} not found", locationId);
                throw new EntityNotFoundException("Location with id " + locationId + " not found");
            }

            Location location = locationOpt.get();
            List<MaintenanceDTO> maintenances = maintenanceMapper.toDtoList(location.getMaintenances().stream().toList());
            log.info("Fetched {} maintenances for location ID: {}", maintenances.size(), locationId);
            return maintenances;
        } catch (Exception e) {
            log.error("Error while fetching maintenances for location ID: {}", locationId, e);
            throw e;
        }
    }
}
