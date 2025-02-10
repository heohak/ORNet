package com.demo.bait.mapper;

import com.demo.bait.dto.MaintenanceCommentDTO;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.MaintenanceComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MaintenanceCommentMapper {

    List<MaintenanceCommentDTO> toDtoList(List<MaintenanceComment> maintenanceComments);

    @Mapping(source = "maintenance.id", target = "maintenanceId")
    @Mapping(source = "device.id", target = "deviceId")
    @Mapping(source = "linkedDevice.id", target = "linkedDeviceId")
    @Mapping(source = "software.id", target = "softwareId")
    @Mapping(target = "fileIds", expression = "java(mapFilesToIds(maintenanceComment.getFiles()))")
    MaintenanceCommentDTO toDto(MaintenanceComment maintenanceComment);

    default List<Integer> mapFilesToIds(Set<FileUpload> files) {
        return files.stream()
                .map(FileUpload::getId)
                .collect(Collectors.toList());
    }
}
