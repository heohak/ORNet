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
        String name,
        String country,
        String city,
        String streetAddress,
        String postalCode,
        String phone,
        String email,
        LocalDate lastMaintenance,
        LocalDate nextMaintenance,
        List<Integer> maintenanceIds,
        List<Integer> commentIds
) {
}
