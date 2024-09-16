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

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LocationSpecificationService {

    private LocationRepo locationRepo;
    private LocationMapper locationMapper;

    public List<LocationDTO> searchLocations(String searchTerm) {
        Specification<Location> combinedSpec = Specification.where(null);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Specification<Location> searchSpec = new LocationSpecification(searchTerm);
            combinedSpec = combinedSpec.and(searchSpec);
        }
        return locationMapper.toDtoList(locationRepo.findAll(combinedSpec));
    }
}
