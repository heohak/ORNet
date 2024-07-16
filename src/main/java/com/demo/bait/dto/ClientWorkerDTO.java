package com.demo.bait.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ClientWorkerDTO(Integer id, String firstName, String lastName, String email, String phoneNumber,
                              String title, Integer clientId, Integer locationId, List<Integer> roleIds) {

}
