package com.demo.bait.controller.ClientWorkerController;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.classificator.ClientWorkerRoleClassificatorDTO;
import com.demo.bait.service.ClientWorkerServices.ClientWorkerRoleService;
import com.demo.bait.service.ClientWorkerServices.ClientWorkerService;
import com.demo.bait.service.ClientWorkerServices.ClientWorkerSpecificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/worker")
public class ClientWorkerGetController {

    public final ClientWorkerService clientWorkerService;
    public final ClientWorkerRoleService clientWorkerRoleService;
    public final ClientWorkerSpecificationService clientWorkerSpecificationService;

    @GetMapping("/all")
    public List<ClientWorkerDTO> getAllWorkers() {
        return clientWorkerService.getAllWorkers();
    }

    @GetMapping("/{clientId}")
    public List<ClientWorkerDTO> getWorkersByClientId(@PathVariable Integer clientId) {
        return clientWorkerService.getWorkersByClientId(clientId);
    }

    @GetMapping("/location/{workerId}")
    public LocationDTO getWorkerLocation(@PathVariable Integer workerId) {
        return clientWorkerService.getWorkerLocation(workerId);
    }

    @GetMapping("/role/{workerId}")
    public List<ClientWorkerRoleClassificatorDTO> getWorkerRole(@PathVariable Integer workerId) {
        return clientWorkerRoleService.getWorkerRoles(workerId);
    }

    @GetMapping("/search")
    public List<ClientWorkerDTO> searchAndFilterClientWorkers(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "roleId", required = false) Integer roleId,
            @RequestParam(value = "clientId", required = false) Integer clientId) {
        return clientWorkerSpecificationService.searchAndFilterClientWorkers(query, roleId, clientId);
    }
}
