package com.demo.bait.controller;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ClientController {

    public final ClientService clientService;

    @PostMapping("/client")
    public ResponseDTO addClient(@RequestBody ClientDTO clientDTO) {
        return clientService.addClient(clientDTO);
    }

    @GetMapping("/client")
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients();
    }

    @DeleteMapping("/client/{id}")
    public ResponseDTO deleteClient(@PathVariable Integer id) {
        return clientService.deleteClient(id);
    }

    @PutMapping("/client/{clientId}/{locationId}")
    public ResponseDTO addLocationToClient(@PathVariable Integer clientId, @PathVariable Integer locationId) {
        return clientService.addLocationToClient(clientId, locationId);
    }

    @GetMapping("/client/locations/{clientId}")
    public List<LocationDTO> getClientLocations(@PathVariable Integer clientId) {
        return clientService.getClientLocations(clientId);
    }
}
