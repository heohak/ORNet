package com.demo.bait.mapper.componentMapper;


import com.demo.bait.components.HIS;
import com.demo.bait.dto.componentDTO.HISDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HISMapper {
    HISDTO toDto(HIS his);
    List<HISDTO> toDtoList(List<HIS> hisList);
}
