package com.demo.bait.controller.ThirdPartyITController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ThirdPartyITServices.ThirdPartyITService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/third-party")
public class ThirdPartyITDeleteController {

    public final ThirdPartyITService thirdPartyITService;

    @DeleteMapping("/{id}")
    public ResponseDTO deleteThirdPartyIT(@PathVariable Integer id) {
        return thirdPartyITService.deleteThirdPartyIT(id);
    }
}
