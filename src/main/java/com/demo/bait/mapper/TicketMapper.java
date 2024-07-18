package com.demo.bait.mapper;

import com.demo.bait.dto.TicketDTO;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.entity.Ticket;
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

//    @Mapping(source = "client.shortName", target = "clientName")
//    @Mapping(source = "client.id", target = "clientId")
//    @Mapping(source = "ticket.id", target = "mainTicketId")
//    @Mapping(source = "location.id", target = "locationId")
//    @Mapping(source = "status.id", target = "statusId")
//    @Mapping(source = "baitWorker.id", target = "baitWorkerId")
//    @Mapping(target = "contactIds", expression = "java(mapContactsToIds(ticket.getContacts()))")
    List<TicketDTO> toDtoList(List<Ticket> tickets);

    @Mapping(source = "client.fullName", target = "clientName")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "ticket.id", target = "mainTicketId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "status.id", target = "statusId")
    @Mapping(source = "baitWorker.id", target = "baitWorkerId")
    @Mapping(target = "contactIds", expression = "java(mapContactsToIds(ticket.getContacts()))")
    @Mapping(target = "maintenanceIds", expression = "java(mapMaintenancesToIds(ticket.getMaintenances()))")
    @Mapping(target = "fileIds", expression = "java(mapFilesToIds(ticket.getFiles()))")
    TicketDTO toDto(Ticket ticket);

    default List<Integer> mapContactsToIds(Set<ClientWorker> contacts) {
        return contacts.stream()
                .map(ClientWorker::getId)
                .collect(Collectors.toList());
    }

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
}
