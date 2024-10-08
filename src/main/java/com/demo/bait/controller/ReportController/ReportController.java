package com.demo.bait.controller.ReportController;

import com.demo.bait.service.ReportService.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private ReportService reportService;

//    ResponseEntity<Resource>
    @GetMapping("/client-tickets")
    public void generateClientTicketsReport(
            @RequestParam("clientId") Integer clientId,
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        reportService.generateClientTicketsReport(clientId, startDate, endDate);
    }
}
