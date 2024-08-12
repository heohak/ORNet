package com.demo.bait.controller.ThirdPartyITController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.service.ThirdPartyITServices.ThirdPartyITService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/third-party")
public class ThirdPartyITPostController {

    public final ThirdPartyITService thirdPartyITService;

    @PostMapping("/add")
    public ThirdPartyITDTO addThirdPartyIT(@RequestBody ThirdPartyITDTO thirdPartyITDTO) {
        return thirdPartyITService.addThirdPartyIT(thirdPartyITDTO);
    }
}
