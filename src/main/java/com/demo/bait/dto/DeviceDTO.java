package com.demo.bait.dto;

import lombok.Builder;

@Builder
public record DeviceDTO(Integer id, Integer clientId, String deviceName, Integer serialNumber) {
}
