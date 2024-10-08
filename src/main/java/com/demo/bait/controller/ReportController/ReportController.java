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

import java.io.IOException;
import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private ReportService reportService;

    @GetMapping("/client-tickets")
    public ResponseEntity<Resource> generateClientTicketsReport(
            @RequestParam("clientId") Integer clientId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("fileName") String fileName) {
        return reportService.generateClientTicketsReport(clientId, startDate, endDate, fileName);
    }
}
