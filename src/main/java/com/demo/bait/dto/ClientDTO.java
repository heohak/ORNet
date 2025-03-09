package com.demo.bait.dto;

import com.demo.bait.entity.Location;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ClientDTO(Integer id, String fullName, String shortName, String country,
                        List<Integer> locationIds, List<Integer> thirdPartyIds,
                        Boolean pathologyClient, Boolean surgeryClient, Boolean editorClient,
                        Boolean otherMedicalDevices, Boolean prospect, Boolean agreement, Boolean activeCustomer,
                        LocalDate lastMaintenance, LocalDate nextMaintenance,
                        List<Integer> maintenanceIds, String maintenanceDescription, Integer contractTermsId,
                        String changedBy) {

}
