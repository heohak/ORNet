package com.demo.bait.controller.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.ClientWorkerRoleClassificatorDTO;
import com.demo.bait.service.classificator.ClientWorkerRoleClassificatorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/worker/classificator")
public class ClientWorkerRoleClassificatorController {

    public final ClientWorkerRoleClassificatorService workerRoleClassificatorService;

    @PostMapping("/add")
    public ResponseDTO addWorkerRoleClassificator(@RequestBody ClientWorkerRoleClassificatorDTO workerRoleDTO) {
        return workerRoleClassificatorService.addWorkerRoleClassificator(workerRoleDTO);
    }

    @GetMapping("/all")
    public List<ClientWorkerRoleClassificatorDTO> getAllWorkerRoleClassificators() {
        return workerRoleClassificatorService.getAllWorkerRoleClassificators();
    }
}
