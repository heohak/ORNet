package com.demo.bait.mapper;

import com.demo.bait.dto.MaintenanceDTO;
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
public interface MaintenanceMapper {

//    @Mapping(target = "fileIds", expression = "java(mapFilesToIds(maintenance.getFiles()))")
    List<MaintenanceDTO> toDtoList(List<Maintenance> maintenances);

    @Mapping(target = "fileIds", expression = "java(mapFilesToIds(maintenance.getFiles()))")
    MaintenanceDTO toDto(Maintenance maintenance);

    default List<Integer> mapFilesToIds(Set<FileUpload> files) {
        return files.stream()
                .map(FileUpload::getId)
                .collect(Collectors.toList());
    }
}
