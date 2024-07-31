package com.demo.bait.controller.ClientWorkerController;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientWorkerServices.ClientWorkerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/worker")
public class ClientWorkerPostController {

    public final ClientWorkerService clientWorkerService;

    @PostMapping("/add")
    public ResponseDTO addWorker(@RequestBody ClientWorkerDTO workerDTO) {
        return clientWorkerService.addWorker(workerDTO);
    }
}
