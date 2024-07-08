package com.demo.bait.dto;

import lombok.Builder;

@Builder
public record LinkedDeviceDTO(Integer id, Integer deviceId, String name, String manufacturer, String productCode,
                              Integer serialNumber, String comment) {
}
