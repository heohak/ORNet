package com.demo.bait.service.LocationServices;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.Location;
import com.demo.bait.mapper.LocationMapper;
import com.demo.bait.repository.LocationRepo;
import com.demo.bait.specification.DeviceSpecification;
import com.demo.bait.specification.LocationSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LocationSpecificationService {

    private LocationRepo locationRepo;
    private LocationMapper locationMapper;

    public List<LocationDTO> searchLocations(String searchTerm) {
        log.info("Searching for locations with search term: '{}'", searchTerm);
        try {
            Specification<Location> combinedSpec = Specification.where(null);

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                log.debug("Adding search specification for term: '{}'", searchTerm);
                Specification<Location> searchSpec = new LocationSpecification(searchTerm);
                combinedSpec = combinedSpec.and(searchSpec);
            }

            List<LocationDTO> locations = locationMapper.toDtoList(locationRepo.findAll(combinedSpec));
            locations.sort(Comparator.comparing(LocationDTO::name, String::compareToIgnoreCase));
            log.info("Found {} locations matching search term: '{}'", locations.size(), searchTerm);
            return locations;
        } catch (Exception e) {
            log.error("Error while searching for locations with search term: '{}'", searchTerm, e);
            throw e;
        }
    }
}
