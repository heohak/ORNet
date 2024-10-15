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
        if (locationDTO.maintenanceIds() != null) {
            Set<Maintenance> maintenances = maintenanceService
                    .maintenanceIdsToMaintenancesSet(locationDTO.maintenanceIds());
            location.setMaintenances(maintenances);
        }
    }

    @Transactional
    public ResponseDTO addMaintenanceToLocation(Integer locationId, MaintenanceDTO maintenanceDTO) {
        Optional<Location> locationOpt = locationRepo.findById(locationId);
        if (locationOpt.isEmpty()) {
            throw new EntityNotFoundException("Location with id " + locationId + " not found");
        }
        Location location = locationOpt.get();
        Integer maintenanceId = Integer.parseInt(maintenanceService.addMaintenance(maintenanceDTO).token());
        location.getMaintenances().add(maintenanceRepo.getReferenceById(maintenanceId));
        locationRepo.save(location);
        return new ResponseDTO("Maintenance added to location successfully");
    }

    public List<MaintenanceDTO> getLocationMaintenances(Integer locationId) {
        Optional<Location> locationOpt = locationRepo.findById(locationId);
        if (locationOpt.isEmpty()) {
            throw new EntityNotFoundException("Location with id " + locationId + " not found");
        }
        Location location = locationOpt.get();
        return maintenanceMapper.toDtoList(location.getMaintenances().stream().toList());
    }
}
