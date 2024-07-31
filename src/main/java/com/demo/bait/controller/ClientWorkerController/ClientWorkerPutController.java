package com.demo.bait.controller.ClientWorkerController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientWorkerServices.ClientWorkerRoleService;
import com.demo.bait.service.ClientWorkerServices.ClientWorkerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/worker")
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
}
