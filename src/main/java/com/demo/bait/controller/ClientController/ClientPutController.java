package com.demo.bait.controller.ClientController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientServices.ClientLocationService;
import com.demo.bait.service.ClientServices.ClientMaintenanceService;
import com.demo.bait.service.ClientServices.ClientService;
import com.demo.bait.service.ClientServices.ClientThirdPartyITService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/client")
public class ClientPutController {

    public final ClientService clientService;
    public final ClientThirdPartyITService clientThirdPartyITService;
    public final ClientMaintenanceService clientMaintenanceService;
    public final ClientLocationService clientLocationService;

    @PutMapping("/{clientId}/{locationId}")
    public ResponseDTO addLocationToClient(@PathVariable Integer clientId, @PathVariable Integer locationId) {
        return clientLocationService.addLocationToClient(clientId, locationId);
    }

    @PutMapping("/third-party/{clientId}/{thirdPartyITId}")
    public ResponseDTO addThirdPartyIT(@PathVariable Integer clientId, @PathVariable Integer thirdPartyITId) {
        return clientThirdPartyITService.addThirdPartyIT(clientId, thirdPartyITId);
    }

    @PutMapping("/maintenance/{clientId}/{maintenanceId}")
    public ResponseDTO addMaintenanceToClient(@PathVariable Integer clientId, @PathVariable Integer maintenanceId) {
        return clientMaintenanceService.addMaintenanceToClient(clientId, maintenanceId);
    }
}
