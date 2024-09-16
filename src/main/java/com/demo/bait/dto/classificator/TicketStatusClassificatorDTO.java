package com.demo.bait.dto.classificator;

import lombok.Builder;

@Builder
public record TicketStatusClassificatorDTO(Integer id, String status, String color) {
}
