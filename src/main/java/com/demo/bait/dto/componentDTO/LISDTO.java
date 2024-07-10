package com.demo.bait.dto.componentDTO;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record LISDTO(String vendorName, String version, LocalDate updateDate) {
}