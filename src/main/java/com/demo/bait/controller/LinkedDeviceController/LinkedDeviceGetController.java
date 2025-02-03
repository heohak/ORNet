package com.demo.bait.controller.LinkedDeviceController;

import com.demo.bait.components.RequestParamParser;
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
    private final RequestParamParser requestParamParser;


    @GetMapping("/all")
    public List<LinkedDeviceDTO> getAllLinkedDevices() {
        return linkedDeviceService.getAllLinkedDevices();
    }

    @GetMapping("/{deviceId}")
    public List<LinkedDeviceDTO> getLinkedDevicesByDeviceId(@PathVariable String deviceId) {
        Integer parsedDeviceId = requestParamParser.parseId(deviceId, "Device ID");
        return linkedDeviceService.getLinkedDevicesByDeviceId(parsedDeviceId);
    }

    @GetMapping("/comment/{linkedDeviceId}")
    public List<CommentDTO> getLinkedDeviceComments(@PathVariable String linkedDeviceId) {
        Integer parsedLinkedDeviceId = requestParamParser.parseId(linkedDeviceId, "Linked Device ID");
        return linkedDeviceCommentService.getLinkedDeviceComments(parsedLinkedDeviceId);
    }

    @GetMapping("/not-used")
    public List<LinkedDeviceDTO> getNotUsedLinkedDevices() {
        return linkedDeviceService.getNotUsedLinkedDevices();
    }

    @GetMapping("/history/{linkedDeviceId}")
    public List<LinkedDeviceDTO> getLinkedDeviceHistory(@PathVariable String linkedDeviceId) {
        Integer parsedLinkedDeviceId = requestParamParser.parseId(linkedDeviceId, "Linked Device ID");
        return linkedDeviceService.getLinkedDeviceHistory(parsedLinkedDeviceId);
    }

    @GetMapping("/device/{linkedDeviceId}")
    public DeviceDTO getLinkedDeviceDevice(@PathVariable String linkedDeviceId) {
        Integer parsedLinkedDeviceId = requestParamParser.parseId(linkedDeviceId, "Linked Device ID");
        return linkedDeviceService.getLinkedDeviceDevice(parsedLinkedDeviceId);
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
