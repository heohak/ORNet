package com.demo.bait.mapper;

import com.demo.bait.dto.PredefinedDeviceNameDTO;
import com.demo.bait.entity.PredefinedDeviceName;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PredefinedDeviceNameMapper {

    List<PredefinedDeviceNameDTO> toDtoList(List<PredefinedDeviceName> deviceNames);
    PredefinedDeviceNameDTO toDto(PredefinedDeviceName deviceName);
}
