package com.demo.bait.mapper.componentMapper;


import com.demo.bait.components.PACS;
import com.demo.bait.dto.componentDTO.PACSDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PACSMapper {
    PACSDTO toDto(PACS pacs);
    List<PACSDTO> toDtoList(List<PACS> pacsList);
}
