package com.demo.bait.mapper;

import com.demo.bait.dto.TicketDTO;
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
public interface TicketMapper {

    List<TicketDTO> toDtoList(List<Ticket> tickets);

    @Mapping(source = "client.fullName", target = "clientName")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "status.id", target = "statusId")
    @Mapping(source = "baitWorker.id", target = "baitWorkerId")
//    @Mapping(source = "paidWork.id", target = "paidWorkId")
    @Mapping(target = "contactIds", expression = "java(mapContactsToIds(ticket.getContacts()))")
    @Mapping(target = "workTypeIds", expression = "java(mapWorkTypesToIds(ticket.getWorkTypes()))")
    @Mapping(target = "commentIds", expression = "java(mapCommentsToIds(ticket.getComments()))")
    @Mapping(target = "fileIds", expression = "java(mapFilesToIds(ticket.getFiles()))")
    @Mapping(target = "deviceIds", expression = "java(mapDevicesToIds(ticket.getDevices()))")
    TicketDTO toDto(Ticket ticket);

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

    default List<Integer> mapCommentsToIds(Set<Comment> comments) {
        return comments.stream()
                .map(Comment::getId)
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
