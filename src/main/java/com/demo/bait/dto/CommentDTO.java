package com.demo.bait.dto;

import lombok.Builder;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
public record CommentDTO(Integer id, String comment, LocalDateTime timestamp, Duration timeSpent) {
}
