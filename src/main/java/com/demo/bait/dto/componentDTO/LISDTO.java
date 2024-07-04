package com.demo.bait.dto.componentDTO;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LISDTO(String vendorName, String version, LocalDateTime updateDate) {
}