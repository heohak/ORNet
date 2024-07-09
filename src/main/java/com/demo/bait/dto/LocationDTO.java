package com.demo.bait.dto;

import lombok.Builder;

@Builder
public record LocationDTO(Integer id, String name, String address, String phone) {
}
