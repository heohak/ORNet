package com.demo.bait.controller.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.ClientWorkerRoleClassificatorDTO;
import com.demo.bait.service.classificator.ClientWorkerRoleClassificatorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/worker/classificator")
public class ClientWorkerRoleClassificatorController {

    public final ClientWorkerRoleClassificatorService workerRoleClassificatorService;

    @PostMapping("/add")
    public ClientWorkerRoleClassificatorDTO addWorkerRoleClassificator(@RequestBody ClientWorkerRoleClassificatorDTO workerRoleDTO) {
        return workerRoleClassificatorService.addWorkerRoleClassificator(workerRoleDTO);
    }

    @GetMapping("/all")
    public List<ClientWorkerRoleClassificatorDTO> getAllWorkerRoleClassificators() {
        return workerRoleClassificatorService.getAllWorkerRoleClassificators();
    }

    @PutMapping("/update/{roleId}")
    public ResponseDTO updateWorkerRole(@PathVariable Integer roleId,
                                        @RequestBody ClientWorkerRoleClassificatorDTO workerRoleDTO) {
        return workerRoleClassificatorService.updateWorkerRoleClassificator(roleId, workerRoleDTO);
    }

    @DeleteMapping("/{roleId}")
    public ResponseDTO deleteWorkerRole(@PathVariable Integer roleId) {
        return workerRoleClassificatorService.deleteWorkerRoleClassificator(roleId);
    }

    @GetMapping("/history/{roleId}")
    public List<ClientWorkerRoleClassificatorDTO> getWorkerRoleClassificatorHistory(@PathVariable Integer roleId) {
        return workerRoleClassificatorService.getWorkerRoleClassificatorHistory(roleId);
    }

    @GetMapping("/deleted")
    public List<ClientWorkerRoleClassificatorDTO> getDeletedRoles() {
        return workerRoleClassificatorService.getDeletedRoles();
    }
}
