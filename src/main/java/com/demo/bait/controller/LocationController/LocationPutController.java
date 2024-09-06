package com.demo.bait.controller.LocationController;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.LocationServices.LocationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/location")
public class LocationPutController {

    public final LocationService locationService;

    @PutMapping("/update/{locationId}")
    public ResponseDTO updateLocation(@PathVariable Integer locationId,@Valid @RequestBody LocationDTO locationDTO) {
        return locationService.updateLocation(locationId, locationDTO);
    }
}
