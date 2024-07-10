package com.demo.bait.dto.componentDTO;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record HL7DTO(String vendorName, String version, LocalDate updateDate) {
}