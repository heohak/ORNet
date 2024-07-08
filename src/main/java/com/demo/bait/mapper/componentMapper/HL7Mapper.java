package com.demo.bait.mapper.componentMapper;


import com.demo.bait.components.HL7;
import com.demo.bait.dto.componentDTO.HL7DTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HL7Mapper {
    HL7DTO toDto(HL7 hl7);
    List<HL7DTO> toDtoList(List<HL7> hl7List);
}
