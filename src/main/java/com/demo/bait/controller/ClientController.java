package com.demo.bait.controller;

import com.demo.bait.dto.ClientDTO;
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
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }


}
