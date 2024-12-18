package com.demo.bait.controller.ClientController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientServices.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/client")
public class ClientDeleteController {

    public final ClientService clientService;

    @DeleteMapping("/delete/{id}")
    public ResponseDTO deleteClient(@PathVariable Integer id) {
        return clientService.deleteClient(id);
    }
}
