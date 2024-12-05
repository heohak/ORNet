package com.demo.bait.controller.BaitWorkerController;

import com.demo.bait.dto.BaitWorkerDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.BaitWorkerServices.BaitWorkerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/bait/worker")
public class BaitWorkerPostController {

    public final BaitWorkerService baitWorkerService;

    @PostMapping("/add")
    public ResponseDTO addWorker(@Valid @RequestBody BaitWorkerDTO workerDTO) {
        return baitWorkerService.addWorker(workerDTO);
    }
}
