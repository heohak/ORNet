package com.demo.bait.controller.ClientActivityController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientActivityService.ClientActivityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/client-activity")
public class ClientActivityDeleteController {

    public final ClientActivityService clientActivityService;

    @DeleteMapping("/delete/{clientActivityId}")
    public ResponseDTO deleteClientActivity(@PathVariable Integer clientActivityId) {
        return clientActivityService.deleteClientActivity(clientActivityId);
    }
}
