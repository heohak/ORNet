package com.demo.bait.controller.BaitWorkerController;

import com.demo.bait.dto.BaitWorkerDTO;
import com.demo.bait.service.BaitWorkerServices.BaitWorkerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/bait/worker")
public class BaitWorkerGetController {

    public final BaitWorkerService baitWorkerService;

    @GetMapping("/all")
    public List<BaitWorkerDTO> getAllWorkers() {
        return baitWorkerService.getAllWorkers();
    }

    @GetMapping("/{workerId}")
    public BaitWorkerDTO getBaitWorkerById(@PathVariable Integer workerId) {
        return baitWorkerService.getBaitWorkerById(workerId);
    }
}
