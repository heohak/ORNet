package com.demo.bait.mapper;

import com.demo.bait.dto.PaidWorkDTO;
import com.demo.bait.entity.PaidWork;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaidWorkMapper {

    List<PaidWorkDTO> toDtoList(List<PaidWork> paidWorks);
    PaidWorkDTO toDto(PaidWork paidWork);
}
