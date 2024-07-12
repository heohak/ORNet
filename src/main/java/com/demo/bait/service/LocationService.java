package com.demo.bait.service;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Location;
import com.demo.bait.mapper.LocationMapper;
import com.demo.bait.repository.LocationRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LocationService {

    private LocationRepo locationRepo;
    private LocationMapper locationMapper;

    public List<LocationDTO> getAllLocations() {
        return locationMapper.toDtoList(locationRepo.findAll());
    }

    public ResponseDTO addLocation(LocationDTO locationDTO) {
        Location location = new Location();
        location.setName(locationDTO.name());
        location.setAddress(locationDTO.address());
        location.setPhone(locationDTO.phone());
        locationRepo.save(location);
        return new ResponseDTO("Location added successfully");
    }

    public ResponseDTO deleteLocation(Integer locationId) {
        locationRepo.deleteById(locationId);
        return new ResponseDTO("Location deleted successfully");
    }
}
