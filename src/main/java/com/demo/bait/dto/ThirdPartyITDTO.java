package com.demo.bait.dto;

import lombok.Builder;

@Builder
public record ThirdPartyITDTO(Integer id, String name, String email, String phone) {
}
