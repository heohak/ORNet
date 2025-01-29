package com.demo.bait.dto;

import lombok.Builder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TicketDTO(Integer id, Integer clientId, String title,
//                        String name,
                        String baitNumeration,
                        String clientNumeration, String description,
                        LocalDateTime startDateTime, Integer locationId,
                        List<Integer> contactIds, List<Integer> workTypeIds,
//                        Boolean remote,
                        Boolean crisis,
                        Integer statusId, Integer baitWorkerId, LocalDateTime responseDateTime,
//                        String response,
                        String insideInfo, LocalDateTime endDateTime, LocalDateTime updateDateTime, String rootCause,
                        String clientName,
//                        List<Integer> commentIds,
                        List<Integer> activityIds,
                        List<Integer> fileIds,
//                        Integer paidWorkId,
                        Boolean paid, Boolean settled, Duration timeSpent, Duration paidTime,
                        List<Integer> deviceIds,
                        String customerRegisterNos) {
}
