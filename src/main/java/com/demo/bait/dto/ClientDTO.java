package com.demo.bait.dto;

import com.demo.bait.entity.Location;
import lombok.Builder;

import java.util.List;

@Builder
public record ClientDTO(Integer id, String fullName, String shortName, String thirdPartyIT, List<Integer> locationIds) {

}
