package com.demo.bait.mapper;

import com.demo.bait.dto.SoftwareDTO;
import com.demo.bait.entity.Software;
import com.demo.bait.mapper.componentMapper.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {HISMapper.class, PACSMapper.class, DICOMMapper.class, HL7Mapper.class, LISMapper.class})
public interface SoftwareMapper {

    SoftwareDTO toDto(Software software);
    List<SoftwareDTO> toDtoList(List<Software> softwareList);
}
