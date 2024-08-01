package com.demo.bait.mapper.classificator;

import com.demo.bait.dto.classificator.WorkTypeClassificatorDTO;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkTypeClassificatorMapper {

    List<WorkTypeClassificatorDTO> toDtoList(List<WorkTypeClassificator> workTypeClassificators);
    WorkTypeClassificatorDTO toDto(WorkTypeClassificator workTypeClassificator);
}
