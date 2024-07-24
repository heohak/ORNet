package com.demo.bait.mapper.classificator;

import com.demo.bait.dto.classificator.DeviceClassificatorDTO;
import com.demo.bait.entity.classificator.DeviceClassificator;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceClassificatorMapper {

    List<DeviceClassificatorDTO> toDtoList(List<DeviceClassificator> deviceClassificatorList);
    DeviceClassificatorDTO toDto(DeviceClassificator deviceClassificator);
}
