package com.demo.bait.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record LinkedDeviceDTO(Integer id, Integer deviceId, String name, String manufacturer, String productCode,
                              Integer serialNumber, String comment, Map<String, Object> attributes) {
}
