package com.demo.bait.controller.LocationController;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.service.LocationServices.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/location")
public class LocationGetController {

    public final LocationService locationService;

    @GetMapping("/all")
    public List<LocationDTO> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{locationId}")
    public LocationDTO getLocationById(@PathVariable Integer locationId) {
        return locationService.getLocationById(locationId);
    }

    @GetMapping("/history/{locationId}")
    public List<LocationDTO> getLocationHistory(@PathVariable Integer locationId) {
        return locationService.getLocationHistory(locationId);
    }
}
