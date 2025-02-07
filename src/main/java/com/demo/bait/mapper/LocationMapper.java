package com.demo.bait.mapper;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.Maintenance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {

    List<LocationDTO> toDtoList(List<Location> locationList);
//    @Mapping(target = "maintenanceIds", expression = "java(mapMaintenancesToIds(location.getMaintenances()))")
    @Mapping(target = "commentIds", expression = "java(mapCommentsToIds(location.getComments()))")
    LocationDTO toDto(Location location);

//    default List<Integer> mapMaintenancesToIds(Set<Maintenance> maintenances) {
//        return maintenances.stream()
//                .map(Maintenance::getId)
//                .collect(Collectors.toList());
//    }

    default List<Integer> mapCommentsToIds(Set<Comment> comments) {
        return comments.stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
    }
}
