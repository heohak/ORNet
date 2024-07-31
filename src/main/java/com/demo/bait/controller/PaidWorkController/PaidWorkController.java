package com.demo.bait.controller.PaidWorkController;

import com.demo.bait.service.PaidWorkServices.PaidWorkService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/paid-work")
public class PaidWorkController {

    public final PaidWorkService paidWorkService;
}
