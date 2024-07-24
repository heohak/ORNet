package com.demo.bait.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record MaintenanceDTO(Integer id, String maintenanceName, LocalDate maintenanceDate, String comment,
                             List<Integer> fileIds) {
}
