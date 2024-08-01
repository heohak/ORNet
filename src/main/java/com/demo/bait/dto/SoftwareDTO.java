package com.demo.bait.dto;

import com.demo.bait.components.*;
import com.demo.bait.dto.componentDTO.*;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SoftwareDTO(Integer id, Integer clientId, String name, String dbVersion, HISDTO his, PACSDTO pacs,
                          DICOMDTO dicom, HL7DTO hl7, LISDTO lis, ReturnImagesToLISDTO returnImagesToLIS,
                          ORNetAPIDTO orNetAPI, LocalDate txtIntegrationDate, CustomerAPIDTO customerAPI,
                          ORNetAPIClientDTO orNetAPIClient, ConsultationModuleDTO consultationModule,
                          AIModuleDTO aiModule) {
}
