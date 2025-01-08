package com.demo.bait.controller.ThirdPartyITController;

import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.service.ThirdPartyITServices.ThirdPartyITFileUploadService;
import com.demo.bait.service.ThirdPartyITServices.ThirdPartyITService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/third-party")
public class ThirdPartyITGetController {

    public final ThirdPartyITService thirdPartyITService;
    public final ThirdPartyITFileUploadService thirdPartyITFileUploadService;

    @GetMapping("/all")
    public List<ThirdPartyITDTO> getAllThirdPartyITs() {
        return thirdPartyITService.getAllThirdParties();
    }

    @GetMapping("/{thirdPartyId}")
    public ThirdPartyITDTO getThirdPartyITById(@PathVariable Integer thirdPartyId) {
        return thirdPartyITService.getThirdPartyITById(thirdPartyId);
    }

    @GetMapping("/files/{thirdPartyId}")
    public List<FileUploadDTO> getThirdPartyITFiles(@PathVariable Integer thirdPartyId) {
        return thirdPartyITFileUploadService.getThirdPartyITFiles(thirdPartyId);
    }
}
