package com.demo.bait.controller.ReportController;

import com.demo.bait.service.ReportService.ClientMaintenanceReportService;
import com.demo.bait.service.ReportService.ClientTicketReportService;
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
@RequestMapping("/api/report")
public class ReportController {

    private ClientTicketReportService clientTicketReportService;
    private ClientMaintenanceReportService clientMaintenanceReportService;

    @GetMapping("/client-tickets")
    public ResponseEntity<Resource> generateClientTicketsReport(
            @RequestParam("clientId") Integer clientId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("fileName") String fileName) {
        return clientTicketReportService.generateClientTicketsReport(clientId, startDate, endDate, fileName);
    }

    @GetMapping("/client-maintenances")
    public ResponseEntity<Resource> generateClientMaintenancesReport(
            @RequestParam("clientId") Integer clientId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("fileName") String fileName) {
        return clientMaintenanceReportService.generateClientMaintenanceReport(clientId, startDate, endDate, fileName);
    }

    @GetMapping("/all-clients-tickets")
    public ResponseEntity<Resource> generateAllClientsTicketsReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("fileName") String fileName) {
        return clientTicketReportService.generateAllClientsTicketsReport(startDate, endDate, fileName);
    }

    @GetMapping("/all-clients-maintenances")
    public ResponseEntity<Resource> generateAllClientsMaintenancesReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("fileName") String fileName) {
        return clientMaintenanceReportService.generateAllClientsMaintenancesReport(startDate, endDate, fileName);
    }
}
