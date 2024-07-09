package com.demo.bait.mapper;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {

    List<LocationDTO> toDtoList(List<Location> locationList);
    LocationDTO toDto(Location location);
    Set<LocationDTO> toDtoList(Set<Location> locationSet);
}
