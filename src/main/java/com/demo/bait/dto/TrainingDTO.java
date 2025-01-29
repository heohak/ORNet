package com.demo.bait.dto;

import com.demo.bait.enums.TrainingType;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record TrainingDTO(Integer id, Integer clientId, Integer locationId, String name, String description,
                          List<Integer> trainersIds, LocalDate trainingDate, TrainingType trainingType) {
}
