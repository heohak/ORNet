package com.demo.bait.controller.ClientController;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientServices.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/client")
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

    @PutMapping("/update/{clientId}")
    public ResponseDTO updateClient(@PathVariable Integer clientId, @RequestBody ClientDTO clientDTO) {
        return clientService.updateClient(clientId, clientDTO);
    }

    @PutMapping("/maintenance/conditions/{clientId}")
    public ResponseDTO updateClientMaintenanceConditions(@PathVariable Integer clientId,
                                                         @RequestBody ClientDTO clientDTO) {
        return clientMaintenanceService.updateClientMaintenanceConditions(clientId, clientDTO);
    }

    @PutMapping("/upload/terms/{clientId}")
    public ResponseDTO uploadContractTermsForClient(@PathVariable Integer clientId,
                                                    @RequestParam("file") MultipartFile file) {
        return clientMaintenanceService.uploadContractTermsForClient(clientId, file);
    }
}
