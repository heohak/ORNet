package com.demo.bait.controller.ClientWorkerController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.ClientDTO;
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
@RequestMapping("/api/worker")
public class ClientWorkerGetController {

    public final ClientWorkerService clientWorkerService;
    public final ClientWorkerRoleService clientWorkerRoleService;
    public final ClientWorkerSpecificationService clientWorkerSpecificationService;
    private RequestParamParser requestParamParser;


    @GetMapping("/all")
    public List<ClientWorkerDTO> getAllWorkers() {
        return clientWorkerService.getAllWorkers();
    }

    @GetMapping("/{clientId}")
    public List<ClientWorkerDTO> getWorkersByClientId(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "clientId");
        return clientWorkerService.getWorkersByClientId(parsedClientId);
    }

    @GetMapping("/location/{workerId}")
    public LocationDTO getWorkerLocation(@PathVariable String workerId) {
        Integer parsedWorkerId = requestParamParser.parseId(workerId, "workerId");
        return clientWorkerService.getWorkerLocation(parsedWorkerId);
    }

    @GetMapping("/role/{workerId}")
    public List<ClientWorkerRoleClassificatorDTO> getWorkerRole(@PathVariable String workerId) {
        Integer parsedWorkerId = requestParamParser.parseId(workerId, "workerId");
        return clientWorkerRoleService.getWorkerRoles(parsedWorkerId);
    }

    @GetMapping("/search")
    public List<ClientWorkerDTO> searchAndFilterClientWorkers(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "roleId", required = false) Integer roleId,
            @RequestParam(value = "clientId", required = false) Integer clientId,
            @RequestParam(value = "favorite", required = false) Boolean favorite,
            @RequestParam(value = "locationId", required = false) Integer locationId,
            @RequestParam(value = "country", required = false) String country) {
        return clientWorkerSpecificationService.searchAndFilterClientWorkers(query, roleId, clientId, favorite,
                locationId, country);
    }

    @GetMapping("/employer/{workerId}")
    public ClientDTO getWorkerEmployer(@PathVariable String workerId) {
        Integer parsedWorkerId = requestParamParser.parseId(workerId, "workerId");
        return clientWorkerService.getWorkerEmployer(parsedWorkerId);
    }

    @GetMapping("/not-used")
    public List<ClientWorkerDTO> getNotUsedContacts() {
        return clientWorkerService.getNotUsedContacts();
    }

    @GetMapping("/id/{workerId}")
    public ClientWorkerDTO getWorkerById(@PathVariable String workerId) {
        Integer parsedWorkerId = requestParamParser.parseId(workerId, "workerId");
        return clientWorkerService.getWorkerById(parsedWorkerId);
    }
}
