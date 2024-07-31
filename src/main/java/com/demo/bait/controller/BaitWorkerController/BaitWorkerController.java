package com.demo.bait.controller.BaitWorkerController;

import com.demo.bait.service.BaitWorkerServices.BaitWorkerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/bait/worker")
public class BaitWorkerController {

    public final BaitWorkerService baitWorkerService;
}
