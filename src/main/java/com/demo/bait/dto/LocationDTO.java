package com.demo.bait.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record LocationDTO(
        Integer id,
//        @NotBlank(message = "Name cannot be empty")
        String name,
//        @NotBlank(message = "Address cannot be empty")
//        String address,
        String country,
        String city,
        String streetAddress,
        String postalCode,
        @Pattern(regexp = "^\\+?[0-9 ]{1,15}$", message = "Invalid phone number format")
        String phone,
        @Email
        String email,
        LocalDate lastMaintenance,
        LocalDate nextMaintenance,
        List<Integer> maintenanceIds,
        List<Integer> commentIds
) {
}
