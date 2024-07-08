package com.demo.bait.dto;

import lombok.Builder;

@Builder
public record BaitWorkerDTO(Integer id, String firstName, String lastName, String email, String phoneNumber,
                            String title) {
}
