package com.demo.bait.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.List;

@Builder
public record ClientWorkerDTO(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String title,
        Integer clientId,
        Integer locationId,
        List<Integer> roleIds,
        Boolean favorite,
        String comment
) {
}
