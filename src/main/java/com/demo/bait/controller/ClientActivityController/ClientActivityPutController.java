package com.demo.bait.controller.ClientActivityController;

import com.demo.bait.dto.ClientActivityDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ClientActivityService.ClientActivityCommentService;
import com.demo.bait.service.ClientActivityService.ClientActivityFileUploadService;
import com.demo.bait.service.ClientActivityService.ClientActivityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/client-activity")
public class ClientActivityPutController {

    public final ClientActivityService clientActivityService;
    public final ClientActivityFileUploadService clientActivityFileUploadService;
    public final ClientActivityCommentService clientActivityCommentService;

    @PutMapping("/update/{clientActivityId}")
    public ResponseDTO updateClientActivity(@PathVariable Integer clientActivityId,
                                            @RequestBody ClientActivityDTO clientActivityDTO) {
        return clientActivityService.updateClientActivity(clientActivityId, clientActivityDTO);
    }

    @PutMapping("/upload/{clientActivityId}")
    public ResponseDTO uploadFilesToClientActivity(@PathVariable Integer clientActivityId,
                                                   @RequestParam("files")List<MultipartFile> files) throws IOException {
        return clientActivityFileUploadService.uploadFilesToClientActivity(clientActivityId, files);
    }

    @PutMapping("/activity/{clientActivityId}")
    public ResponseDTO addActivityToClientActivity(@PathVariable Integer clientActivityId,
                                                   @RequestBody String activity) {
        return clientActivityCommentService.addActivityToClientActivity(clientActivityId, activity);
    }
}
