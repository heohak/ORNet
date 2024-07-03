package com.demo.bait.dto;

import lombok.Builder;

@Builder
public record TicketDTO(Integer id, Integer clientId, String description) {
}
