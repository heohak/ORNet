package com.demo.bait.service.MaintenanceServices;

import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.repository.MaintenanceRepo;
import com.demo.bait.specification.MaintenanceSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MaintenanceSpecificationService {

    private MaintenanceRepo maintenanceRepo;
    private MaintenanceMapper maintenanceMapper;

    public List<MaintenanceDTO> searchAndFilterMaintenances(String searchTerm, Integer locationId, Integer baitWorkerId,
                                                            Integer deviceId, Integer linkedDeviceId,
                                                            Integer softwareId, Integer clientId) {
        log.info("Searching and filtering maintenances");

        Specification<Maintenance> combinedSpec = Specification.where(null);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            log.debug("Adding search term specification: {}", searchTerm);
            combinedSpec = combinedSpec.and(new MaintenanceSpecification(searchTerm));
        }

        if (locationId != null) {
            log.debug("Adding location ID specification: {}", locationId);
            combinedSpec = combinedSpec.and(MaintenanceSpecification.hasLocationId(locationId));
        }

        if (baitWorkerId != null) {
            log.debug("Adding bait worker ID specification: {}", baitWorkerId);
            combinedSpec = combinedSpec.and(MaintenanceSpecification.hasBaitWorkerId(baitWorkerId));
        }

        if (deviceId != null) {
            log.debug("Adding device ID specification: {}", deviceId);
            combinedSpec = combinedSpec.and(MaintenanceSpecification.hasDeviceId(deviceId));
        }

        if (linkedDeviceId != null) {
            log.debug("Adding linked device ID specification: {}", linkedDeviceId);
            combinedSpec = combinedSpec.and(MaintenanceSpecification.hasLinkedDeviceId(linkedDeviceId));
        }

        if (softwareId != null) {
            log.debug("Adding software ID specification: {}", softwareId);
            combinedSpec = combinedSpec.and(MaintenanceSpecification.hasSoftwareId(softwareId));
        }

        if (clientId != null) {
            log.debug("Adding client ID specification: {}", clientId);
            combinedSpec = combinedSpec.and(MaintenanceSpecification.hasClientId(clientId));
        }

        List<Maintenance> filteredMaintenances = maintenanceRepo.findAll(combinedSpec,
                Sort.by(Sort.Direction.DESC, "maintenanceDate"));

        log.info("Found {} maintenances matching criteria", filteredMaintenances.size());

        return maintenanceMapper.toDtoList(filteredMaintenances);
    }
}
