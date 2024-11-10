package com.demo.bait.dto;

import lombok.Builder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CommentDTO(Integer id, String comment, LocalDateTime timestamp) {
}
