package com.demo.bait.controller.LinkedDeviceController;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.LinkedDeviceDTO;
import com.demo.bait.service.LinkedDeviceServices.LinkedDeviceCommentService;
import com.demo.bait.service.LinkedDeviceServices.LinkedDeviceService;
import com.demo.bait.service.LinkedDeviceServices.LinkedDeviceSpecificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/linked/device")
public class LinkedDeviceGetController {

    public final LinkedDeviceService linkedDeviceService;
    public final LinkedDeviceCommentService linkedDeviceCommentService;
    public final LinkedDeviceSpecificationService linkedDeviceSpecificationService;

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

    @GetMapping("/device/{linkedDeviceId}")
    public DeviceDTO getLinkedDeviceDevice(@PathVariable Integer linkedDeviceId) {
        return linkedDeviceService.getLinkedDeviceDevice(linkedDeviceId);
    }

    @GetMapping("/search")
    public List<LinkedDeviceDTO> searchAndFilterLinkedDevices(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "locationId", required = false) Integer locationId,
            @RequestParam(value = "deviceId", required = false) Integer deviceId,
            @RequestParam(value = "template", required = false) Boolean template) {
        return linkedDeviceSpecificationService.searchAndFilterLinkedDevices(query, locationId, deviceId, template);
    }
}
