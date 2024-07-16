package com.demo.bait.dto.componentDTO;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CustomerAPIDTO(String vendorName, String version, LocalDate updateDate) {
}
