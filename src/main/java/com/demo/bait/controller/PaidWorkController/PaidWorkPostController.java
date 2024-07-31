package com.demo.bait.controller.PaidWorkController;

import com.demo.bait.service.PaidWorkServices.PaidWorkService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/paid-work")
public class PaidWorkPostController {

    public final PaidWorkService paidWorkService;

    @PostMapping("/create")
    public void createPaidWork() {
        paidWorkService.createPaidWork();
    }
}
