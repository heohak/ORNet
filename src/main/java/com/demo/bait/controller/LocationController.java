package com.demo.bait.controller;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/location")
public class LocationController {

    public final LocationService locationService;

    @PostMapping("/add")
    public ResponseDTO addLocation(@RequestBody LocationDTO locationDTO) {
        return locationService.addLocation(locationDTO);
    }

    @GetMapping("/all")
    public List<LocationDTO> getAllLocations() {
        return locationService.getAllLocations();
    }

    @DeleteMapping("/{locationId}")
    public ResponseDTO deleteLocation(@PathVariable Integer locationId) {
        return locationService.deleteLocation(locationId);
    }

    @GetMapping("/{locationId}")
    public LocationDTO getLocationById(@PathVariable Integer locationId) {
        return locationService.getLocationById(locationId);
    }
}
