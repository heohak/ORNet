package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TicketDTO;
import com.demo.bait.dto.classificator.WorkTypeClassificatorDTO;
import com.demo.bait.entity.Ticket;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.classificator.WorkTypeClassificatorMapper;
import com.demo.bait.repository.TicketRepo;
import com.demo.bait.repository.classificator.WorkTypeClassificatorRepo;
import com.demo.bait.service.classificator.WorkTypeClassificatorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class TicketWorkTypeService {

    private TicketRepo ticketRepo;
    private WorkTypeClassificatorMapper workTypeClassificatorMapper;
    private WorkTypeClassificatorService workTypeClassificatorService;

    @Transactional
    public void addWorkTypeToTicket(Ticket ticket, TicketDTO ticketDTO) {
        log.info("Adding work types to ticket with ID: {}", ticket.getId());

        if (ticketDTO.workTypeIds() != null) {
            log.debug("Work type IDs provided: {}", ticketDTO.workTypeIds());
            Set<WorkTypeClassificator> workTypes = workTypeClassificatorService
                    .workTypeIdsToWorkTypesSet(ticketDTO.workTypeIds());
            ticket.setWorkTypes(workTypes);
            ticketRepo.save(ticket);
            log.info("Work types successfully added to ticket with ID: {}", ticket.getId());
        } else {
            log.warn("No work type IDs provided for ticket with ID: {}", ticket.getId());
        }
    }

    public List<WorkTypeClassificatorDTO> getTicketWorkTypes(Integer ticketId) {
        if (ticketId == null) {
            log.warn("Ticket ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        log.info("Fetching work types for ticket with ID: {}", ticketId);

        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            log.error("Ticket with ID {} not found", ticketId);
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        List<WorkTypeClassificatorDTO> workTypes = workTypeClassificatorMapper.toDtoList(ticket.getWorkTypes().stream().toList());
        log.info("Found {} work types for ticket with ID: {}", workTypes.size(), ticketId);
        return workTypes;
    }
}
