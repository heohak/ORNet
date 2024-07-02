package com.demo.bait.dto;

import lombok.Builder;

@Builder
public record ClientDTO(Integer id, String fullName, String shortName) {

}
