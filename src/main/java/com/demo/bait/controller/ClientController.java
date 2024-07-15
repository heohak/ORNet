package com.demo.bait.controller;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/client")
public class ClientController {

    public final ClientService clientService;

    @PostMapping("/add")
    public ResponseDTO addClient(@RequestBody ClientDTO clientDTO) {
        return clientService.addClient(clientDTO);
    }

    @GetMapping("/all")
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO deleteClient(@PathVariable Integer id) {
        return clientService.deleteClient(id);
    }

    @PutMapping("/{clientId}/{locationId}")
    public ResponseDTO addLocationToClient(@PathVariable Integer clientId, @PathVariable Integer locationId) {
        return clientService.addLocationToClient(clientId, locationId);
    }

    @GetMapping("/locations/{clientId}")
    public List<LocationDTO> getClientLocations(@PathVariable Integer clientId) {
        return clientService.getClientLocations(clientId);
    }

    @PutMapping("/third-party/{clientId}/{thirdPartyITId}")
    public ResponseDTO addThirdPartyIT(@PathVariable Integer clientId, @PathVariable Integer thirdPartyITId) {
        return clientService.addThirdPartyIT(clientId, thirdPartyITId);
    }

    @GetMapping("/third-parties/{clientId}")
    public List<ThirdPartyITDTO> getClientThirdPartyITs(@PathVariable Integer clientId) {
        return clientService.getClientThirdPartyITs(clientId);
    }

    @GetMapping("/{clientId}")
    public ClientDTO getClientById(@PathVariable Integer clientId) {
        return clientService.getClientById(clientId);
    }
}
