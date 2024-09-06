package com.demo.bait.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record LocationDTO(
        Integer id,
        @NotBlank(message = "Name cannot be empty")
        String name,
        @NotBlank(message = "Address cannot be empty")
        String address,
        @Pattern(regexp = "^\\+?[0-9 ]{1,15}$", message = "Invalid phone number format")
        String phone
) {
}
