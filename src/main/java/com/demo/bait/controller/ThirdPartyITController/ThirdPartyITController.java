package com.demo.bait.controller.ThirdPartyITController;

import com.demo.bait.service.ThirdPartyITServices.ThirdPartyITService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/third-party")
public class ThirdPartyITController {

    public final ThirdPartyITService thirdPartyITService;
}
