package com.demo.bait.controller.LocationController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.LocationServices.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class LocationDeleteController {

    public final LocationService locationService;

    @DeleteMapping("/location/{locationId}")
    public ResponseDTO deleteLocation(@PathVariable Integer locationId) {
        return locationService.deleteLocation(locationId);
    }

    @DeleteMapping("/admin/location/force/{locationId}")
    public ResponseDTO forceDeleteLocation(@PathVariable Integer locationId) {
        return locationService.forceDeleteLocation(locationId);
    }
}
