package com.demo.bait.controller.ClientWorkerController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientWorkerServices.ClientWorkerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/worker")
public class ClientWorkerDeleteController {

    public final ClientWorkerService clientWorkerService;

    @DeleteMapping("/{workerId}")
    public ResponseDTO deleteWorker(@PathVariable Integer workerId) {
        return clientWorkerService.deleteWorker(workerId);
    }
}
