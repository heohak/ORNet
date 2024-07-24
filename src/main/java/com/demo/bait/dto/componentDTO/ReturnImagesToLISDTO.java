package com.demo.bait.dto.componentDTO;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ReturnImagesToLISDTO(Boolean toReturn, String link, LocalDate updateDate) {
}
