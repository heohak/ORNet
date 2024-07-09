package com.demo.bait.controller;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.entity.ThirdPartyIT;
import com.demo.bait.service.ThirdPartyITService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/third-party")
public class ThirdPartyITController {

    public final ThirdPartyITService thirdPartyITService;

    @PostMapping("/add")
    public ResponseDTO addThirdPartyIT(@RequestBody ThirdPartyITDTO thirdPartyITDTO) {
        return thirdPartyITService.addThirdPartyIT(thirdPartyITDTO);
    }

    @GetMapping("/all")
    public List<ThirdPartyITDTO> getAllThirdPartyITs() {
        return thirdPartyITService.getAllThirdParties();
    }

    @DeleteMapping("/{id}")
    public ResponseDTO deleteThirdPartyIT(@PathVariable Integer id) {
        return thirdPartyITService.deleteThirdPartyIT(id);
    }
}
