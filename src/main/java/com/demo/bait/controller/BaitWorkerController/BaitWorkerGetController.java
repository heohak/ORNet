package com.demo.bait.controller.BaitWorkerController;

import com.demo.bait.components.RequestParamParser;
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
    private RequestParamParser requestParamParser;


    @GetMapping("/all")
    public List<BaitWorkerDTO> getAllWorkers() {
        return baitWorkerService.getAllWorkers();
    }

    @GetMapping("/{workerId}")
    public BaitWorkerDTO getBaitWorkerById(@PathVariable String workerId) {
        Integer parsedWorkerId = requestParamParser.parseId(workerId, "workerId");
        return baitWorkerService.getBaitWorkerById(parsedWorkerId);
    }
}
