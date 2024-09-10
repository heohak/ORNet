package com.demo.bait.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record BaitWorkerDTO(
        Integer id,

        @NotBlank(message = "First name cannot be empty")
        String firstName,

        @NotBlank(message = "Last name cannot be empty")
        String lastName,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be empty")
        String email,

        @Pattern(regexp = "^\\+?[0-9 ]{1,15}$", message = "Invalid phone number format")
        String phoneNumber,

        @NotBlank(message = "Title cannot be empty")
        String title
) {
}
