package com.demo.bait.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TicketDTO(Integer id, Integer clientId, String title, String description, Integer mainTicketId,
                        LocalDateTime startDateTime, Integer locationId, List<Integer> contactIds, String workType,
                        Boolean remote, Boolean crisis, Integer statusId, Integer baitWorkerId,
                        LocalDateTime responseDateTime, String response, String insideInfo, LocalDateTime endDateTime,
                        String rootCause, String clientName, List<Integer> commentIds, List<Integer> maintenanceIds,
                        List<Integer> fileIds) {
}
