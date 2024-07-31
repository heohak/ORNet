package com.demo.bait.controller.LocationController;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.LocationServices.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/location")
public class LocationPostController {

    public final LocationService locationService;

    @PostMapping("/add")
    public ResponseDTO addLocation(@RequestBody LocationDTO locationDTO) {
        return locationService.addLocation(locationDTO);
    }
}
