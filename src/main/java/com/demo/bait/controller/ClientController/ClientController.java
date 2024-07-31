package com.demo.bait.controller.ClientController;

import com.demo.bait.service.ClientServices.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/client")
public class ClientController {

    public final ClientService clientService;
}
