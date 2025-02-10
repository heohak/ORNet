package com.demo.bait.controller.LocationController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.service.LocationServices.LocationCommentService;
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
//    public final LocationMaintenanceService locationMaintenanceService;
    public final LocationCommentService locationCommentService;
    private final RequestParamParser requestParamParser;


    @GetMapping("/all")
    public List<LocationDTO> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{locationId}")
    public LocationDTO getLocationById(@PathVariable String locationId) {
        Integer parsedLocationId = requestParamParser.parseId(locationId, "Location ID");
        return locationService.getLocationById(parsedLocationId);
    }

    @GetMapping("/history/{locationId}")
    public List<LocationDTO> getLocationHistory(@PathVariable String locationId) {
        Integer parsedLocationId = requestParamParser.parseId(locationId, "Location ID");
        return locationService.getLocationHistory(parsedLocationId);
    }

    @GetMapping("/search")
    public List<LocationDTO> searchLocations(@RequestParam(value = "q", required = false) String query) {
        return locationSpecificationService.searchLocations(query);
    }

//    @GetMapping("/maintenances/{locationId}")
//    public List<MaintenanceDTO> getLocationMaintenances(@PathVariable String locationId) {
//        Integer parsedLocationId = requestParamParser.parseId(locationId, "Location ID");
//        return locationMaintenanceService.getLocationMaintenances(parsedLocationId);
//    }

    @GetMapping("/comments/{locationId}")
    public List<CommentDTO> getLocationComments(@PathVariable String locationId) {
        Integer parsedLocationId = requestParamParser.parseId(locationId, "Location ID");
        return locationCommentService.getLocationComments(parsedLocationId);
    }

    @GetMapping("/countries")
    public List<String> getAllLocationCountries() {
        return locationService.getAllLocationCountries();
    }
}
