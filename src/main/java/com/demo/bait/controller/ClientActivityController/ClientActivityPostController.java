package com.demo.bait.controller.ClientActivityController;

import com.demo.bait.dto.ClientActivityDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientActivityService.ClientActivityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/client-activity")
public class ClientActivityPostController {

    public final ClientActivityService clientActivityService;

    @PostMapping("/add")
    public ResponseDTO addClientActivity(@RequestBody ClientActivityDTO clientActivityDTO) {
        return clientActivityService.addClientActivity(clientActivityDTO);
    }
}
