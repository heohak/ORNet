package com.demo.bait.controller;

import com.demo.bait.dto.BaitWorkerDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.BaitWorkerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class BaitWorkerController {

    public final BaitWorkerService baitWorkerService;

    @PostMapping("/bait/worker")
    public ResponseDTO addWorker(@RequestBody BaitWorkerDTO workerDTO) {
        return baitWorkerService.addWorker(workerDTO);
    }

    @GetMapping("/bait/workers")
    public List<BaitWorkerDTO> getAllWorkers() {
        return baitWorkerService.getAllWorkers();
    }

    @DeleteMapping("/bait/worker/{workerId}")
    public ResponseDTO deleteWorker(@PathVariable Integer workerId) {
        return baitWorkerService.deleteBaitWorker(workerId);
    }
}
