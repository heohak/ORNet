package com.demo.bait.dto;

import lombok.Builder;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
public record ActivityDTO(Integer id, String activity, LocalDateTime timestamp, Duration timeSpent, Boolean paid,
                          String username) {
}
