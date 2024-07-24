package com.demo.bait.dto.componentDTO;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ORNetAPIDTO(String version, LocalDate updateDate) {
}
