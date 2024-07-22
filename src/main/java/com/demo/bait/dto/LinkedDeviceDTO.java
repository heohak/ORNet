package com.demo.bait.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record LinkedDeviceDTO(Integer id, Integer deviceId, String name, String manufacturer, String productCode,
                              Integer serialNumber, List<Integer> commentIds, Map<String, Object> attributes) {
}
