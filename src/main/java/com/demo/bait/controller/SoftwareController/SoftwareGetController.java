package com.demo.bait.controller.SoftwareController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.SoftwareDTO;
import com.demo.bait.service.SoftwareServices.SoftwareService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/software")
public class SoftwareGetController {

    public final SoftwareService softwareService;
    private final RequestParamParser requestParamParser;


    @GetMapping("/all")
    public List<SoftwareDTO> getAllSoftwareVariations() {
        return softwareService.getAllSoftwareVariations();
    }

    @GetMapping("/client/{clientId}")
    public List<SoftwareDTO> getSoftwareByClientId(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "Client ID");
        return softwareService.getSoftwareByClientId(parsedClientId);
    }

    @GetMapping("/not-used")
    public List<SoftwareDTO> getNotUsedSoftware() {
        return softwareService.getNotUsedSoftware();
    }

    @GetMapping("/{softwareId}")
    public SoftwareDTO getSoftwareById(@PathVariable String softwareId) {
        Integer parsedSoftwareId = requestParamParser.parseId(softwareId, "Software ID");
        return softwareService.getSoftwareById(parsedSoftwareId);
    }
}
