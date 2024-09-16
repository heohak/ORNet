package com.demo.bait.controller.LocationController;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.service.LocationServices.LocationService;
import com.demo.bait.service.LocationServices.LocationSpecificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/location")
public class LocationGetController {

    public final LocationService locationService;
    public final LocationSpecificationService locationSpecificationService;

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

    @GetMapping("/search")
    public List<LocationDTO> searchLocations(@RequestParam(value = "q", required = false) String query) {
        return locationSpecificationService.searchLocations(query);
    }
}
