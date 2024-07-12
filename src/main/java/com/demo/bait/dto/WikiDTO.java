package com.demo.bait.dto;

import lombok.Builder;

@Builder
public record WikiDTO(Integer id, String problem, String solution) {
}
