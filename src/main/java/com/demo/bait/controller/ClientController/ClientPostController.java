package com.demo.bait.controller.ClientController;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientServices.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/client")
public class ClientPostController {

    public final ClientService clientService;

    @PostMapping("/add")
    public ResponseDTO addClient(@RequestBody ClientDTO clientDTO) {
        return clientService.addClient(clientDTO);
    }
}
