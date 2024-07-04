package com.demo.bait.mapper.componentMapper;


import com.demo.bait.components.LIS;
import com.demo.bait.dto.componentDTO.LISDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LISMapper {
    LISDTO toDto(LIS lis);
    List<LISDTO> toDtoList(List<LIS> lisList);
}
