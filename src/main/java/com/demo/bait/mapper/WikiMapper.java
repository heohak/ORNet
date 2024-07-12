package com.demo.bait.mapper;

import com.demo.bait.dto.WikiDTO;
import com.demo.bait.entity.Wiki;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WikiMapper {

    List<WikiDTO> toDtoList(List<Wiki> wikis);
    WikiDTO toDto(Wiki wiki);
}
