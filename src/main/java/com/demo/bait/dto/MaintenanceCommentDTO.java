package com.demo.bait.dto;

import com.demo.bait.enums.MaintenanceStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record MaintenanceCommentDTO(Integer id, Integer maintenanceId, Integer deviceId, Integer linkedDeviceId,
                                    Integer softwareId, String comment,
                                    MaintenanceStatus maintenanceStatus, List<Integer> fileIds) {
}
