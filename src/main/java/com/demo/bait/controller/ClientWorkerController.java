package com.demo.bait.controller;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.ClientWorkerRoleClassificatorDTO;
import com.demo.bait.service.ClientWorkerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/worker")
public class ClientWorkerController {

    public final ClientWorkerService clientWorkerService;

    @PostMapping("/add")
    public ResponseDTO addWorker(@RequestBody ClientWorkerDTO workerDTO) {
        return clientWorkerService.addWorker(workerDTO);
    }

    @GetMapping("/all")
    public List<ClientWorkerDTO> getAllWorkers() {
        return clientWorkerService.getAllWorkers();
    }

    @PutMapping("/{workerId}/{clientId}")
    public ResponseDTO addEmployer(@PathVariable Integer workerId, @PathVariable Integer clientId) {
        return clientWorkerService.addEmployer(workerId, clientId);
    }

    @GetMapping("/{clientId}")
    public List<ClientWorkerDTO> getWorkersByClientId(@PathVariable Integer clientId) {
        return clientWorkerService.getWorkersByClientId(clientId);
    }

    @DeleteMapping("/{workerId}")
    public ResponseDTO deleteWorker(@PathVariable Integer workerId) {
        return clientWorkerService.deleteWorker(workerId);
    }

    @PutMapping("/location/{workerId}/{locationId}")
    public ResponseDTO addLocation(@PathVariable Integer workerId, @PathVariable Integer locationId) {
        return clientWorkerService.addLocationToEmployee(workerId, locationId);
    }

    @PutMapping("/role/{workerId}/{roleId}")
    public ResponseDTO addRole(@PathVariable Integer workerId, @PathVariable Integer roleId) {
        return clientWorkerService.addRoleToEmployee(workerId, roleId);
    }

    @GetMapping("/location/{workerId}")
    public LocationDTO getWorkerLocation(@PathVariable Integer workerId) {
        return clientWorkerService.getWorkerLocation(workerId);
    }

    @GetMapping("/role/{workerId}")
    public List<ClientWorkerRoleClassificatorDTO> getWorkerRole(@PathVariable Integer workerId) {
        return clientWorkerService.getWorkerRole(workerId);
    }
}
