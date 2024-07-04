package com.demo.bait.mapper.componentMapper;


import com.demo.bait.components.DICOM;
import com.demo.bait.dto.componentDTO.DICOMDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DICOMMapper {
    DICOMDTO toDto(DICOM dicom);
    List<DICOMDTO> toDtoList(List<DICOM> dicomList);
}
