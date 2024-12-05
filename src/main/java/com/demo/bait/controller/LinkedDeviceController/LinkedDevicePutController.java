package com.demo.bait.controller.LinkedDeviceController;

import com.demo.bait.dto.LinkedDeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.LinkedDeviceServices.LinkedDeviceAttributeService;
import com.demo.bait.service.LinkedDeviceServices.LinkedDeviceCommentService;
import com.demo.bait.service.LinkedDeviceServices.LinkedDeviceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/linked/device")
public class LinkedDevicePutController {

    public final LinkedDeviceService linkedDeviceService;
    public final LinkedDeviceCommentService linkedDeviceCommentService;
    public final LinkedDeviceAttributeService linkedDeviceAttributeService;

    @PutMapping("/link/{linkedDeviceId}/{deviceId}")
    public ResponseDTO linkDevice(@PathVariable Integer linkedDeviceId, @PathVariable Integer deviceId) {
        return linkedDeviceService.linkDevice(linkedDeviceId, deviceId);
    }

    @PutMapping("/{linkedDeviceId}/attributes")
    public ResponseDTO updateLinkedDeviceAttributes(@PathVariable Integer linkedDeviceId,
                                                    @RequestBody Map<String, Object> attributes) {
        return linkedDeviceAttributeService.updateLinkedDeviceAttributes(linkedDeviceId, attributes);
    }

    @PutMapping("/comment/{linkedDeviceId}")
    public ResponseDTO addCommentToLinkedDevice(@PathVariable Integer linkedDeviceId,
                                                @RequestParam("comment") String comment) {
        return linkedDeviceCommentService.addCommentToLinkedDevice(linkedDeviceId, comment);
    }

    @PutMapping("/update/{linkedDeviceId}")
    public ResponseDTO updateLinkedDevice(@PathVariable Integer linkedDeviceId,
                                          @RequestBody LinkedDeviceDTO linkedDeviceDTO) {
        return linkedDeviceService.updateLinkedDevice(linkedDeviceId, linkedDeviceDTO);
    }

    @PutMapping("/remove/{linkedDeviceId}")
    public ResponseDTO removeDeviceFromLinkedDevice(@PathVariable Integer linkedDeviceId) {
        return linkedDeviceService.removeDeviceFromLinkedDevice(linkedDeviceId);
    }
}
