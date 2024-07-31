package com.demo.bait.controller.MaintenanceController;

import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/maintenance")
public class MaintenanceController {

    public final MaintenanceService maintenanceService;
}
