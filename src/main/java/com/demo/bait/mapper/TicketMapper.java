package com.demo.bait.mapper;

import com.demo.bait.dto.TicketDTO;
import com.demo.bait.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    List<TicketDTO> toDtoList(List<Ticket> tickets);

    TicketDTO toDto(Ticket ticket);
}
