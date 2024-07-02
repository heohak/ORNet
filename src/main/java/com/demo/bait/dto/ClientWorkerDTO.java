package com.demo.bait.dto;

import lombok.Builder;

@Builder
public record ClientWorkerDTO(Integer id, String firstName, String lastName, String email, String phoneNumber,
                              String title, Integer clientId) {

}
