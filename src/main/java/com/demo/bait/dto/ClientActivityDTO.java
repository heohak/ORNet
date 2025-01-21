package com.demo.bait.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ClientActivityDTO(Integer id, Integer clientId, String title, String description,
                                LocalDateTime startDateTime, Integer locationId, List<Integer> contactIds,
                                List<Integer> workTypeIds, Boolean crisis, Integer statusId, Integer baitWorkerId,
                                LocalDateTime endDateTime, LocalDateTime updateDateTime,
                                List<Integer> fileIds,
                                String clientName, List<Integer> activityIds) {
}
