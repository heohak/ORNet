package com.demo.bait.dto;

import lombok.Builder;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
public record ActivityDTO(Integer id, String comment, LocalDateTime timestamp, Duration timeSpent, Boolean paid) {
}
