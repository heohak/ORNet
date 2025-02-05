package com.demo.bait.mapper;

import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.entity.*;
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

    List<MaintenanceDTO> toDtoList(List<Maintenance> maintenances);

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "baitWorker.id", target = "baitWorkerId")
    @Mapping(target = "fileIds", expression = "java(mapFilesToIds(maintenance.getFiles()))")
    @Mapping(target = "deviceIds", expression = "java(mapDevicesToIds(maintenance.getDevices()))")
    @Mapping(target = "linkedDeviceIds", expression = "java(mapLinkedDevicesToIds(maintenance.getLinkedDevices()))")
    @Mapping(target = "softwareIds", expression = "java(mapSoftwareToIds(maintenance.getSoftwares()))")
    MaintenanceDTO toDto(Maintenance maintenance);

    default List<Integer> mapFilesToIds(Set<FileUpload> files) {
        return files.stream()
                .map(FileUpload::getId)
                .collect(Collectors.toList());
    }
    default List<Integer> mapDevicesToIds(Set<Device> devices) {
        return devices.stream()
                .map(Device::getId)
                .collect(Collectors.toList());
    }
    default List<Integer> mapLinkedDevicesToIds(Set<LinkedDevice> linkedDevices) {
        return linkedDevices.stream()
                .map(LinkedDevice::getId)
                .collect(Collectors.toList());
    }
    default List<Integer> mapSoftwareToIds(Set<Software> softwares) {
        return softwares.stream()
                .map(Software::getId)
                .collect(Collectors.toList());
    }
}
