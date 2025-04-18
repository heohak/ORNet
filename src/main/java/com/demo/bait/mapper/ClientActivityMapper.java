package com.demo.bait.mapper;

import com.demo.bait.dto.ClientActivityDTO;
import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientActivityMapper {

    List<ClientActivityDTO> toDtoList(List<ClientActivity> clientActivities);

    @Mapping(source = "client.fullName", target = "clientName")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "status.id", target = "statusId")
    @Mapping(source = "baitWorker.id", target = "baitWorkerId")
    @Mapping(target = "contactIds", expression = "java(mapContactsToIds(clientActivity.getContacts()))")
    @Mapping(target = "workTypeIds", expression = "java(mapWorkTypesToIds(clientActivity.getWorkTypes()))")
    @Mapping(target = "fileIds", expression = "java(mapFilesToIds(clientActivity.getFiles()))")
    @Mapping(target = "activityIds", expression = "java(mapActivitiesToIds(clientActivity.getActivities()))")
    ClientActivityDTO toDto(ClientActivity clientActivity);

    default List<Integer> mapActivitiesToIds(Set<Activity> activities) {
        return activities.stream()
                .map(Activity::getId)
                .collect(Collectors.toList());
    }

    default List<Integer> mapContactsToIds(Set<ClientWorker> contacts) {
        return contacts.stream()
                .map(ClientWorker::getId)
                .collect(Collectors.toList());
    }

    default List<Integer> mapFilesToIds(Set<FileUpload> files) {
        return files.stream()
                .map(FileUpload::getId)
                .collect(Collectors.toList());
    }

    default List<Integer> mapWorkTypesToIds(Set<WorkTypeClassificator> workTypes) {
        return workTypes.stream()
                .map(WorkTypeClassificator::getId)
                .collect(Collectors.toList());
    }

    default List<Integer> mapDevicesToIds(Set<Device> devices) {
        return devices.stream()
                .map(Device::getId)
                .collect(Collectors.toList());
    }
}
