package com.demo.bait.controller.LocationController;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.service.LocationServices.LocationCommentService;
import com.demo.bait.service.LocationServices.LocationMaintenanceService;
import com.demo.bait.service.LocationServices.LocationService;
import com.demo.bait.service.LocationServices.LocationSpecificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/location")
public class LocationGetController {

    public final LocationService locationService;
    public final LocationSpecificationService locationSpecificationService;
    public final LocationMaintenanceService locationMaintenanceService;
    public final LocationCommentService locationCommentService;

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

    @GetMapping("/maintenances/{locationId}")
    public List<MaintenanceDTO> getLocationMaintenances(@PathVariable Integer locationId) {
        return locationMaintenanceService.getLocationMaintenances(locationId);
    }

    @GetMapping("/comments/{locationId}")
    public List<CommentDTO> getLocationComments(@PathVariable Integer locationId) {
        return locationCommentService.getLocationComments(locationId);
    }

    @GetMapping("/countries")
    public List<String> getAllLocationCountries() {
        return locationService.getAllLocationCountries();
    }
}
