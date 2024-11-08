package com.demo.bait.controller.ClientActivityController;

import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.service.ClientActivityService.ClientActivityFileUploadService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/client-activity")
public class ClientActivityGetController {

    public final ClientActivityFileUploadService clientActivityFileUploadService;

    @GetMapping("/files/{clientActivityId}")
    public List<FileUploadDTO> getClientActivityFiles(@PathVariable Integer clientActivityId) {
        return clientActivityFileUploadService.getClientActivityFiles(clientActivityId);
    }
}
