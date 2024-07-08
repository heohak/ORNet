package com.demo.bait.mapper;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.entity.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceMapper {

    @Mapping(source = "client.id", target = "clientId")
    List<DeviceDTO> toDtoList(List<Device> devices);

    @Mapping(source = "client.id", target = "clientId")
    DeviceDTO toDto(Device device);
}
