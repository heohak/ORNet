package com.demo.bait.controller.ClientWorkerController;

import com.demo.bait.service.ClientWorkerServices.ClientWorkerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/worker")
public class ClientWorkerController {

    public final ClientWorkerService clientWorkerService;
}
