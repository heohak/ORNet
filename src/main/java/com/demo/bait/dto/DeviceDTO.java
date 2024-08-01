package com.demo.bait.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
public record DeviceDTO(Integer id, Integer clientId, Integer locationId, String deviceName, Integer classificatorId,
                        String department, String room, String serialNumber, String licenseNumber, String version,
                        LocalDate versionUpdateDate, List<Integer> maintenanceIds, String firstIPAddress,
                        String secondIPAddress, String subnetMask, String softwareKey, LocalDate introducedDate,
                        LocalDate writtenOffDate, List<Integer> commentIds, List<Integer> fileIds,
                        Map<String, Object> attributes) {
}
