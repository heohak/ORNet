package com.demo.bait.mapper;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    List<CommentDTO> toDtoList(List<Comment> comments);

    @Mapping(target = "locationIds", expression = "java(mapLocationsToIds(comment.getLocations()))")
    CommentDTO toDto(Comment comment);

    default List<Integer> mapLocationsToIds(Set<Location> locations) {
        return locations.stream()
                .map(Location::getId)
                .collect(Collectors.toList());
    }
}
