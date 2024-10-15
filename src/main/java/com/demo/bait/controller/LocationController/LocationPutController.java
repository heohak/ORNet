package com.demo.bait.controller.LocationController;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.LocationServices.LocationCommentService;
import com.demo.bait.service.LocationServices.LocationMaintenanceService;
import com.demo.bait.service.LocationServices.LocationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/location")
public class LocationPutController {

    public final LocationService locationService;
    public final LocationCommentService locationCommentService;
    public final LocationMaintenanceService locationMaintenanceService;

    @PutMapping("/update/{locationId}")
    public ResponseDTO updateLocation(@PathVariable Integer locationId,@Valid @RequestBody LocationDTO locationDTO) {
        return locationService.updateLocation(locationId, locationDTO);
    }

    @PutMapping("/maintenance/{locationId}")
    public ResponseDTO addMaintenanceToLocation(@PathVariable Integer locationId,
                                                @RequestBody MaintenanceDTO maintenanceDTO) {
        return locationMaintenanceService.addMaintenanceToLocation(locationId, maintenanceDTO);
    }

    @PutMapping("/comment/{locationId}")
    public ResponseDTO commentLocation(@PathVariable Integer locationId, @RequestParam("comment") String comment) {
        return locationCommentService.addCommentToLocation(locationId, comment);
    }
}
