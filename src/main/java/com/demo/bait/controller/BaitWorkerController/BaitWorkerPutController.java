package com.demo.bait.controller.BaitWorkerController;

import com.demo.bait.dto.BaitWorkerDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.BaitWorkerServices.BaitWorkerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/bait/worker")
public class BaitWorkerPutController {

    public final BaitWorkerService baitWorkerService;

    @PutMapping("/update/{baitWorkerId}")
    public ResponseDTO updateBaitWorker(@PathVariable Integer baitWorkerId,
                                        @Valid @RequestBody BaitWorkerDTO baitWorkerDTO) {
        return baitWorkerService.updateBaitWorker(baitWorkerId, baitWorkerDTO);
    }
}
