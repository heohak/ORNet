package com.demo.bait.controller;

import com.demo.bait.dto.PaidWorkDTO;
import com.demo.bait.service.PaidWorkService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/paid-work")
public class PaidWorkController {

    public final PaidWorkService paidWorkService;

    @PostMapping("/create")
    public void createPaidWork() {
        paidWorkService.createPaidWork();
    }

    @PutMapping("/time/{paidWorkId}")
    public PaidWorkDTO addTimeToPaidWork(@PathVariable Integer paidWorkId,
                                         @RequestParam(value = "hours", required = false) Integer hours,
                                         @RequestParam(value = "minutes", required = false) Integer minutes) {
        return paidWorkService.addTimeToPaidWork(paidWorkId, hours, minutes);
    }
}
