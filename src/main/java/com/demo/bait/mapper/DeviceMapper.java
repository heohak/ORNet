package com.demo.bait.mapper;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.FileUpload;
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
public interface DeviceMapper {

//    @Mapping(source = "client.id", target = "clientId")
//    @Mapping(source = "location.id", target = "locationId")
//    @Mapping(source = "classificator.id", target = "classificatorId")
//    @Mapping(target = "maintenanceIds", expression = "java(mapMaintenancesToIds(device.getMaintenances()))")
//    @Mapping(target = "fileIds", expression = "java(mapFilesToIds(device.getFiles()))")
    List<DeviceDTO> toDtoList(List<Device> devices);

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "classificator.id", target = "classificatorId")
    @Mapping(target = "maintenanceIds", expression = "java(mapMaintenancesToIds(device.getMaintenances()))")
    @Mapping(target = "commentIds", expression = "java(mapCommentsToIds(device.getComments()))")
    @Mapping(target = "fileIds", expression = "java(mapFilesToIds(device.getFiles()))")
    DeviceDTO toDto(Device device);

    default List<Integer> mapMaintenancesToIds(Set<Maintenance> maintenances) {
        return maintenances.stream()
                .map(Maintenance::getId)
                .collect(Collectors.toList());
    }

    default List<Integer> mapFilesToIds(Set<FileUpload> files) {
        return files.stream()
                .map(FileUpload::getId)
                .collect(Collectors.toList());
    }

    default List<Integer> mapCommentsToIds(Set<Comment> comments) {
        return comments.stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
    }
}
