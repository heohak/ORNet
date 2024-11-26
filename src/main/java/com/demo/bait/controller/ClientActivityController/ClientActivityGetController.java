package com.demo.bait.controller.ClientActivityController;

import com.demo.bait.dto.ClientActivityDTO;
import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.service.ClientActivityService.ClientActivityFileUploadService;
import com.demo.bait.service.ClientActivityService.ClientActivitySpecificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/client-activity")
public class ClientActivityGetController {

    public final ClientActivityFileUploadService clientActivityFileUploadService;
    public final ClientActivitySpecificationService clientActivitySpecificationService;

    @GetMapping("/files/{clientActivityId}")
    public List<FileUploadDTO> getClientActivityFiles(@PathVariable Integer clientActivityId) {
        return clientActivityFileUploadService.getClientActivityFiles(clientActivityId);
    }

    @GetMapping("/search")
    public List<ClientActivityDTO> searchClientActivities(
            @RequestParam(value = "statusId", required = false) Integer statusId) {
        return clientActivitySpecificationService.searchAndFilterClientActivities(statusId);
    }
}
