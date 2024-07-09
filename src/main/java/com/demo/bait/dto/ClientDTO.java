package com.demo.bait.dto;

import com.demo.bait.entity.Location;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ClientDTO(Integer id, String fullName, String shortName,
                        List<Integer> locationIds, List<Integer> thirdPartyIds,
                        Boolean pathologyClient, Boolean surgeryClient, Boolean editorClient,
                        String otherMedicalInformation, LocalDateTime lastMaintenance, LocalDateTime nextMaintenance) {

}
