package com.demo.bait.controller.ThirdPartyITController;

import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.service.ThirdPartyITServices.ThirdPartyITService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/third-party")
public class ThirdPartyITGetController {

    public final ThirdPartyITService thirdPartyITService;

    @GetMapping("/all")
    public List<ThirdPartyITDTO> getAllThirdPartyITs() {
        return thirdPartyITService.getAllThirdParties();
    }
}
