package com.demo.bait.controller.ThirdPartyITController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.service.ThirdPartyITServices.ThirdPartyITService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/third-party")
public class ThirdPartyITPutController {

    public final ThirdPartyITService thirdPartyITService;

    @PutMapping("/update/{thirdPartyId}")
    public ResponseDTO updateThirdPartyIT(@PathVariable Integer thirdPartyId,
                                          @RequestBody ThirdPartyITDTO thirdPartyITDTO) {
        return thirdPartyITService.updateThirdPartyIT(thirdPartyId, thirdPartyITDTO);
    }
}
