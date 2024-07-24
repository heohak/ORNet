package com.demo.bait.mapper.classificator;

import com.demo.bait.dto.classificator.TicketStatusClassificatorDTO;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketStatusClassificatorMapper {

    List<TicketStatusClassificatorDTO> toDtoList(List<TicketStatusClassificator> ticketStatusClassificators);
    TicketStatusClassificatorDTO toDto(TicketStatusClassificator ticketStatusClassificator);
}
