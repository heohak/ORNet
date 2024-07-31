package com.demo.bait.controller.BaitWorkerController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.BaitWorkerServices.BaitWorkerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/bait/worker")
public class BaitWorkerDeleteController {

    public final BaitWorkerService baitWorkerService;

    @DeleteMapping("/{workerId}")
    public ResponseDTO deleteWorker(@PathVariable Integer workerId) {
        return baitWorkerService.deleteBaitWorker(workerId);
    }
}
