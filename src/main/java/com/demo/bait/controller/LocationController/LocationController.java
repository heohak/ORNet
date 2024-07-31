package com.demo.bait.controller.LocationController;

import com.demo.bait.service.LocationServices.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/location")
public class LocationController {

    public final LocationService locationService;
}
