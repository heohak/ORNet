package com.demo.bait.controller.AuditController;

import com.demo.bait.service.AuditServices.AuditCleanupService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/audit")
public class AuditDeleteController {

    public final AuditCleanupService auditCleanupService;

    @DeleteMapping("/delete")
    public void cleanupAuditTables(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-M-d") LocalDate date) {
        auditCleanupService.cleanupAudTables(date);
    }
}
