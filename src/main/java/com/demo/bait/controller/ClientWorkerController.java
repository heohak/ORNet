package com.demo.bait.controller;

import com.demo.bait.dto.ClientWorkerDTO;
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
    public void addEmployer(@PathVariable Integer workerId, @PathVariable Integer clientId) {
        clientWorkerService.addEmployer(workerId, clientId);
    }
}
