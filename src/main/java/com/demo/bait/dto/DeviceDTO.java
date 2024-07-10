package com.demo.bait.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record DeviceDTO(Integer id, Integer clientId, Integer locationId, String deviceName, String department,
                        String room, Integer serialNumber, String licenseNumber, String version,
                        LocalDate versionUpdateDate, List<Integer> maintenanceIds, String firstIPAddress,
                        String secondIPAddress, String softwareKey, LocalDate introducedDate, LocalDate writtenOffDate,
                        String comment, List<Integer> fileIds) {
}
