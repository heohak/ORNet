package com.demo.bait.dto;

import lombok.Builder;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
public record PaidWorkDTO(Integer id, LocalDateTime startTime, Duration timeSpent, Boolean settled) {
}
