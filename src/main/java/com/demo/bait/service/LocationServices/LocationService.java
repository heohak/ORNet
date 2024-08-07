package com.demo.bait.service.LocationServices;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Location;
import com.demo.bait.mapper.LocationMapper;
import com.demo.bait.repository.LocationRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class LocationService {

    private LocationRepo locationRepo;
    private LocationMapper locationMapper;

    @Transactional
    public LocationDTO addLocation(LocationDTO locationDTO) {
        Location location = new Location();
        location.setName(locationDTO.name());
        location.setAddress(locationDTO.address());
        location.setPhone(locationDTO.phone());
        locationRepo.save(location);
        return locationMapper.toDto(location);
    }

    @Transactional
    public ResponseDTO deleteLocation(Integer locationId) {
        locationRepo.deleteById(locationId);
        return new ResponseDTO("Location deleted successfully");
    }

    @Transactional
    public ResponseDTO updateLocation(Integer locationId, LocationDTO locationDTO) {
        Optional<Location> locationOpt = locationRepo.findById(locationId);
        if (locationOpt.isEmpty()) {
            throw new EntityNotFoundException("Location with id " + locationId + " not found");
        }
        Location location = locationOpt.get();

        updateName(location, locationDTO);
        updateAddress(location, locationDTO);
        updatePhone(location, locationDTO);
        locationRepo.save(location);
        return new ResponseDTO("Location updated successfully");
    }

    public void updateName(Location location, LocationDTO locationDTO) {
        if (locationDTO.name() != null) {
            location.setName(locationDTO.name());
        }
    }

    public void updateAddress(Location location, LocationDTO locationDTO) {
        if (locationDTO.address() != null) {
            location.setAddress(locationDTO.address());
        }
    }

    public void updatePhone(Location location, LocationDTO locationDTO) {
        if (locationDTO.phone() != null) {
            location.setPhone(locationDTO.phone());
        }
    }

    public LocationDTO getLocationById(Integer locationId) {
        Optional<Location> locationOpt = locationRepo.findById(locationId);
        if (locationOpt.isEmpty()) {
            throw new EntityNotFoundException("Location with id " + locationId + " not found");
        }
        return locationMapper.toDto(locationOpt.get());
    }

    public List<LocationDTO> getAllLocations() {
        return locationMapper.toDtoList(locationRepo.findAll());
    }

    public Set<Location> locationIdsToLocationsSet(List<Integer> locationIds) {
        Set<Location> locations = new HashSet<>();
        for (Integer locationId : locationIds) {
            Location location = locationRepo.findById(locationId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid location ID: " + locationId));
            locations.add(location);
        }
        return locations;
    }
}
