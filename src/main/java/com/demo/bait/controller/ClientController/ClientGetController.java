package com.demo.bait.controller.ClientController;

import com.demo.bait.dto.*;
import com.demo.bait.service.ClientServices.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/api/client")
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
     * @param clientTypes should be a string "pathology" or "surgery" or "editor" or "other" or "prospect" of "agreement"
     */
    @GetMapping("/search")
    public List<ClientDTO> searchAndFilterClients(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "clientTypes", required = false) List<String> clientTypes,
            @RequestParam(value = "locationId", required = false) Integer locationId,
            @RequestParam(value = "thirdPartyId", required = false) Integer thirdPartyId,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "activeCustomer", required = false) Boolean activeCustomer) {
        return clientSpecificationService.searchAndFilterClients(query, clientTypes, locationId, thirdPartyId, country,
                activeCustomer);
    }

    @GetMapping("/history/{clientId}")
    public List<ClientDTO> getClientHistory(@PathVariable Integer clientId) {
        return clientService.getClientHistory(clientId);
    }

    @GetMapping("/activities/{clientId}")
    public List<ClientActivityDTO> getClientActivitiesForClient(@PathVariable Integer clientId) {
        return clientService.getClientActivitiesForClient(clientId);
    }

    @GetMapping("/countries")
    public List<String> getAllClientCountries() {
        return clientService.getAllClientCountries();
    }

    @GetMapping("/activity/dates")
    public Map<Integer, Map<String, LocalDateTime>> getClientsActivityDates() {
        return clientService.getClientsActivityDates();
    }


//    @GetMapping("/location/history/{clientId}")
//    public List<ClientLocationHistoryDTO> getClientLocationHistory(@PathVariable Integer clientId) {
//        return clientLocationService.getClientLocationHistory(clientId);
//    }
}
