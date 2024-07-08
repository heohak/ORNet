package com.demo.bait.dto;

import com.demo.bait.dto.componentDTO.*;
import lombok.Builder;

@Builder
public record SoftwareDTO(Integer id, Integer clientId, String name, String dbVersion,
                          HISDTO his, PACSDTO pacs, DICOMDTO dicom, HL7DTO hl7, LISDTO lis) {
}
