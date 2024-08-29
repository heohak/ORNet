package com.demo.bait.controller.LinkedDeviceController;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.LinkedDeviceDTO;
import com.demo.bait.service.LinkedDeviceServices.LinkedDeviceCommentService;
import com.demo.bait.service.LinkedDeviceServices.LinkedDeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/linked/device")
public class LinkedDeviceGetController {

    public final LinkedDeviceService linkedDeviceService;
    public final LinkedDeviceCommentService linkedDeviceCommentService;

    @GetMapping("/all")
    public List<LinkedDeviceDTO> getAllLinkedDevices() {
        return linkedDeviceService.getAllLinkedDevices();
    }

    @GetMapping("/{deviceId}")
    public List<LinkedDeviceDTO> getLinkedDevicesByDeviceId(@PathVariable Integer deviceId) {
        return linkedDeviceService.getLinkedDevicesByDeviceId(deviceId);
    }

    @GetMapping("/comment/{linkedDeviceId}")
    public List<CommentDTO> getLinkedDeviceComments(@PathVariable Integer linkedDeviceId) {
        return linkedDeviceCommentService.getLinkedDeviceComments(linkedDeviceId);
    }

    @GetMapping("/not-used")
    public List<LinkedDeviceDTO> getNotUsedLinkedDevices() {
        return linkedDeviceService.getNotUsedLinkedDevices();
    }

    @GetMapping("/history/{linkedDeviceId}")
    public List<LinkedDeviceDTO> getLinkedDeviceHistory(@PathVariable Integer linkedDeviceId) {
        return linkedDeviceService.getLinkedDeviceHistory(linkedDeviceId);
    }
}
