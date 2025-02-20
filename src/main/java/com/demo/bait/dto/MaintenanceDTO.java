package com.demo.bait.dto;

import com.demo.bait.enums.MaintenanceStatus;
import lombok.Builder;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Builder
public record MaintenanceDTO(Integer id, String maintenanceName, LocalDate maintenanceDate, LocalDate lastDate,
                             String description, List<Integer> fileIds, Integer locationId,
                             MaintenanceStatus maintenanceStatus, Duration timeSpent,
                             Integer baitWorkerId, List<Integer> deviceIds, List<Integer> linkedDeviceIds,
                             List<Integer> softwareIds, String internalComment) {
}
