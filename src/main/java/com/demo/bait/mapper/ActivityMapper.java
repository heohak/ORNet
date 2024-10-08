package com.demo.bait.mapper;

import com.demo.bait.dto.ActivityDTO;
import com.demo.bait.entity.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityMapper {

    List<ActivityDTO> toDtoList(List<Activity> activities);
    ActivityDTO toDto(Activity activity);
}
