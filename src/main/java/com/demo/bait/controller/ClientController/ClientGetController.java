package com.demo.bait.controller.ClientController;

import com.demo.bait.dto.*;
import com.demo.bait.service.ClientServices.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
            @RequestParam(value = "clientType", required = false) String clientType,
            @RequestParam(value = "locationId", required = false) Integer locationId,
            @RequestParam(value = "thirdPartyId", required = false) Integer thirdPartyId,
            @RequestParam(value = "country", required = false) String country) {
        return clientSpecificationService.searchAndFilterClients(query, clientType, locationId, thirdPartyId, country);
    }

    @GetMapping("/history/{clientId}")
    public List<ClientDTO> getClientHistory(@PathVariable Integer clientId) {
        return clientService.getClientHistory(clientId);
    }

    @GetMapping("/activities/{clientId}")
    public List<ClientActivityDTO> getClientActivitiesForClient(@PathVariable Integer clientId) {
        return clientService.getClientActivitiesForClient(clientId);
    }


//    @GetMapping("/location/history/{clientId}")
//    public List<ClientLocationHistoryDTO> getClientLocationHistory(@PathVariable Integer clientId) {
//        return clientLocationService.getClientLocationHistory(clientId);
//    }
}
