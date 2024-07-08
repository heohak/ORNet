package com.demo.bait.mapper;

import com.demo.bait.dto.LinkedDeviceDTO;
import com.demo.bait.entity.LinkedDevice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LinkedDeviceMapper {

    @Mapping(source = "device.id", target = "deviceId")
    List<LinkedDeviceDTO> toDtoList(List<LinkedDevice> linkedDevice);

    @Mapping(source = "device.id", target = "deviceId")
    LinkedDeviceDTO toDto(LinkedDevice linkedDevice);
}
