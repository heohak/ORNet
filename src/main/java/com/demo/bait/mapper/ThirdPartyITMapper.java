package com.demo.bait.mapper;

import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.entity.ThirdPartyIT;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ThirdPartyITMapper {

    List<ThirdPartyITDTO> toDtoList(List<ThirdPartyIT> thirdPartyITList);
    ThirdPartyITDTO toDto(ThirdPartyIT thirdPartyIT);
}
