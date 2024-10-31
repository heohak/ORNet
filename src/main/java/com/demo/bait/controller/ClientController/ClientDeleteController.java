package com.demo.bait.controller.ClientController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientServices.ClientCommentService;
import com.demo.bait.service.ClientServices.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/client")
public class ClientDeleteController {

    public final ClientService clientService;
    public final ClientCommentService clientCommentService;

    @DeleteMapping("/delete/{id}")
    public ResponseDTO deleteClient(@PathVariable Integer id) {
        return clientService.deleteClient(id);
    }

    @DeleteMapping("/delete/comment/{clientId}/{commentId}")
    public ResponseDTO deleteCommentFromClient(@PathVariable Integer clientId, @PathVariable Integer commentId) {
        return clientCommentService.deleteClientComment(clientId, commentId);
    }
}
