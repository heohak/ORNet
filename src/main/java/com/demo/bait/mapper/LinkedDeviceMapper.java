package com.demo.bait.mapper;

import com.demo.bait.dto.LinkedDeviceDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.LinkedDevice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LinkedDeviceMapper {

    List<LinkedDeviceDTO> toDtoList(List<LinkedDevice> linkedDevice);

    @Mapping(source = "device.id", target = "deviceId")
    @Mapping(target = "commentIds", expression = "java(mapCommentsToIds(linkedDevice.getComments()))")
    LinkedDeviceDTO toDto(LinkedDevice linkedDevice);

    default List<Integer> mapCommentsToIds(Set<Comment> comments) {
        return comments.stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
    }
}
