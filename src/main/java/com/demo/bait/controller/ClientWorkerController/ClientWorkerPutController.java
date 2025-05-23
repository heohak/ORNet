package com.demo.bait.controller.ClientWorkerController;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientWorkerServices.ClientWorkerRoleService;
import com.demo.bait.service.ClientWorkerServices.ClientWorkerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/worker")
public class ClientWorkerPutController {

    public final ClientWorkerService clientWorkerService;
    public final ClientWorkerRoleService clientWorkerRoleService;

    @PutMapping("/{workerId}/{clientId}")
    public ResponseDTO addEmployer(@PathVariable Integer workerId, @PathVariable Integer clientId) {
        return clientWorkerService.addEmployer(workerId, clientId);
    }

    @PutMapping("/location/{workerId}/{locationId}")
    public ResponseDTO addLocation(@PathVariable Integer workerId, @PathVariable Integer locationId) {
        return clientWorkerService.addLocationToEmployee(workerId, locationId);
    }

    @PutMapping("/role/{workerId}/{roleId}")
    public ResponseDTO addRole(@PathVariable Integer workerId, @PathVariable Integer roleId) {
        return clientWorkerRoleService.addRoleToEmployee(workerId, roleId);
    }

    @PutMapping("/role/{workerId}")
    public ResponseDTO addRoles(@PathVariable Integer workerId, @RequestBody ClientWorkerDTO clientWorkerDTO) {
        return clientWorkerRoleService.addRolesToWorker(workerId, clientWorkerDTO);
    }

    @PutMapping("/update/{workerId}")
    public ResponseDTO updateClientWorker(@PathVariable Integer workerId,
                                          @RequestBody ClientWorkerDTO clientWorkerDTO) {
        return clientWorkerService.updateClientWorker(workerId, clientWorkerDTO);
    }

    @PutMapping("/favorite/{workerId}")
    public ResponseDTO updateFavorite(@PathVariable Integer workerId) {
        return clientWorkerService.toggleFavorite(workerId);
    }

    @PutMapping("/remove/{workerId}")
    public ResponseDTO removeClientFromWorker(@PathVariable Integer workerId) {
        return clientWorkerService.removeClientFromWorker(workerId);
    }
}
