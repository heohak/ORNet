package com.demo.bait.controller.ClientController;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.service.ClientServices.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/client")
public class ClientGetController {

    public final ClientService clientService;
    public final ClientThirdPartyITService clientThirdPartyITService;
    public final ClientMaintenanceService clientMaintenanceService;
    public final ClientSpecificationService clientSpecificationService;
    public final ClientLocationService clientLocationService;

    @GetMapping("/all")
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{clientId}")
    public ClientDTO getClientById(@PathVariable Integer clientId) {
        return clientService.getClientById(clientId);
    }

    @GetMapping("/locations/{clientId}")
    public List<LocationDTO> getClientLocations(@PathVariable Integer clientId) {
        return clientLocationService.getClientLocations(clientId);
    }

    @GetMapping("/third-parties/{clientId}")
    public List<ThirdPartyITDTO> getClientThirdPartyITs(@PathVariable Integer clientId) {
        return clientThirdPartyITService.getClientThirdPartyITs(clientId);
    }

    @GetMapping("/maintenance/{clientId}")
    public List<MaintenanceDTO> getClientMaintenances(@PathVariable Integer clientId) {
        return clientMaintenanceService.getClientMaintenances(clientId);
    }

    /**
     * Searches and filters tickets
     *
     * @param clientType should be a string "pathology" or "surgery" or "editor"
     */
    @GetMapping("/search")
    public List<ClientDTO> searchAndFilterClients(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "clientType", required = false) String clientType) {
        return clientSpecificationService.searchAndFilterClients(query, clientType);
    }
}
