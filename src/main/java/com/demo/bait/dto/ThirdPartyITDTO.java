package com.demo.bait.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ThirdPartyITDTO(Integer id, String name, String country, String city, String streetAddress,
                              Integer contactId, String email, String phone, List<Integer> fileIds) {
}
