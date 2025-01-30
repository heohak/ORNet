package com.demo.bait.controller.ClientActivityController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.ActivityDTO;
import com.demo.bait.dto.ClientActivityDTO;
import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.service.ClientActivityService.ClientActivityCommentService;
import com.demo.bait.service.ClientActivityService.ClientActivityFileUploadService;
import com.demo.bait.service.ClientActivityService.ClientActivitySpecificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/client-activity")
public class ClientActivityGetController {

    public final ClientActivityFileUploadService clientActivityFileUploadService;
    public final ClientActivitySpecificationService clientActivitySpecificationService;
    public final ClientActivityCommentService clientActivityCommentService;
    private RequestParamParser requestParamParser;


    @GetMapping("/files/{clientActivityId}")
    public List<FileUploadDTO> getClientActivityFiles(@PathVariable String clientActivityId) {
        Integer parsedClientActivityId = requestParamParser.parseId(clientActivityId, "clientActivityId");
        return clientActivityFileUploadService.getClientActivityFiles(parsedClientActivityId);
    }

    @GetMapping("/search")
    public List<ClientActivityDTO> searchClientActivities(
            @RequestParam(value = "statusId", required = false) Integer statusId,
            @RequestParam(value = "clientId", required = false) Integer clientId) {
        return clientActivitySpecificationService.searchAndFilterClientActivities(statusId, clientId);
    }

    @GetMapping("/activity/{clientActivityId}")
    public List<ActivityDTO> getClientActivityActivities(@PathVariable String clientActivityId) {
        Integer parsedClientActivityId = requestParamParser.parseId(clientActivityId, "clientActivityId");
        return clientActivityCommentService.getClientActivityActivities(parsedClientActivityId);
    }
}
