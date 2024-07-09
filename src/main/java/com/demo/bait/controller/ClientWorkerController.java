package com.demo.bait.controller;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientWorkerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ClientWorkerController {

    public final ClientWorkerService clientWorkerService;

    @PostMapping("/worker")
    public ResponseDTO addWorker(@RequestBody ClientWorkerDTO workerDTO) {
        return clientWorkerService.addWorker(workerDTO);
    }

    @GetMapping("/worker")
    public List<ClientWorkerDTO> getAllWorkers() {
        return clientWorkerService.getAllWorkers();
    }

    @PutMapping("/worker/{workerId}/{clientId}")
    public ResponseDTO addEmployer(@PathVariable Integer workerId, @PathVariable Integer clientId) {
        return clientWorkerService.addEmployer(workerId, clientId);
    }

    @GetMapping("/workers/{clientId}")
    public List<ClientWorkerDTO> getWorkersByClientId(@PathVariable Integer clientId) {
        return clientWorkerService.getWorkersByClientId(clientId);
    }

    @DeleteMapping("/workers/{workerId}")
    public ResponseDTO deleteWorker(@PathVariable Integer workerId) {
        return clientWorkerService.deleteWorker(workerId);
    }

    @PutMapping("/worker/location/{workerId}/{locationId}")
    public ResponseDTO addLocation(@PathVariable Integer workerId, @PathVariable Integer locationId) {
        return clientWorkerService.addLocationToEmployee(workerId, locationId);
    }

    @GetMapping("/worker/location/{workerId}")
    public LocationDTO getWorkerLocation(@PathVariable Integer workerId) {
        return clientWorkerService.getWorkerLocation(workerId);
    }
}
