package com.demo.bait.controller.ClientController;

import com.demo.bait.components.RequestParamParser;
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
    private RequestParamParser requestParamParser;


    @GetMapping("/all")
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{clientId}")
    public ClientDTO getClientById(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "clientId");
        return clientService.getClientById(parsedClientId);
    }

    @GetMapping("/locations/{clientId}")
    public List<LocationDTO> getClientLocations(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "clientId");
        return clientLocationService.getClientLocations(parsedClientId);
    }

    @GetMapping("/third-parties/{clientId}")
    public List<ThirdPartyITDTO> getClientThirdPartyITs(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "clientId");
        return clientThirdPartyITService.getClientThirdPartyITs(parsedClientId);
    }

    @GetMapping("/maintenance/{clientId}")
    public List<MaintenanceDTO> getClientMaintenances(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "clientId");
        return clientMaintenanceService.getClientMaintenances(parsedClientId);
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
    public List<ClientDTO> getClientHistory(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "clientId");
        return clientService.getClientHistory(parsedClientId);
    }

    @GetMapping("/activities/{clientId}")
    public List<ClientActivityDTO> getClientActivitiesForClient(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "clientId");
        return clientService.getClientActivitiesForClient(parsedClientId);
    }

    @GetMapping("/countries")
    public List<String> getAllClientCountries() {
        return clientService.getAllClientCountries();
    }

    @GetMapping("/activity/dates")
    public Map<Integer, Map<String, LocalDateTime>> getClientsActivityDates() {
        return clientService.getClientsActivityDates();
    }

    @GetMapping("/terms/{clientId}")
    public FileUploadDTO getClientContractTerms(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "clientId");
        return clientMaintenanceService.getClientContractTerms(parsedClientId);
    }

    @GetMapping("/maintenance/description/{clientId}")
    public ResponseDTO getClientMaintenanceDescription(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "clientId");
        return clientMaintenanceService.getClientMaintenanceDescription(parsedClientId);
    }

//    @GetMapping("/location/history/{clientId}")
//    public List<ClientLocationHistoryDTO> getClientLocationHistory(@PathVariable Integer clientId) {
//        return clientLocationService.getClientLocationHistory(clientId);
//    }
}
