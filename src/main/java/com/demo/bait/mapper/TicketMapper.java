package com.demo.bait.mapper;

import com.demo.bait.dto.TicketDTO;
import com.demo.bait.entity.ClientWorker;
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
    TicketDTO toDto(Ticket ticket);

    default List<Integer> mapContactsToIds(Set<ClientWorker> contacts) {
        return contacts.stream()
                .map(ClientWorker::getId)
                .collect(Collectors.toList());
    }
}
