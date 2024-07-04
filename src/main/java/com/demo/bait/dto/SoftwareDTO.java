package com.demo.bait.dto;

import com.demo.bait.components.DICOM;
import com.demo.bait.components.HIS;
import com.demo.bait.components.HL7;
import com.demo.bait.components.LIS;
import com.demo.bait.dto.componentDTO.*;
import lombok.Builder;

@Builder
public record SoftwareDTO(Integer id, String name, String dbVersion,
                          HISDTO his, PACSDTO pacs, DICOMDTO dicom, HL7DTO hl7, LISDTO lis) {
}
