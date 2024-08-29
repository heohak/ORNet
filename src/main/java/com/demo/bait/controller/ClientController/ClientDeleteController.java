package com.demo.bait.controller.ClientController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientServices.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/client")
public class ClientDeleteController {

    public final ClientService clientService;

    @DeleteMapping("/delete/{id}")
    public ResponseDTO deleteClient(@PathVariable Integer id) {
        return clientService.deleteClient(id);
    }
}
