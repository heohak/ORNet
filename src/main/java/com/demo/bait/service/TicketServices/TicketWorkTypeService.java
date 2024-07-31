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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class TicketWorkTypeService {

    private TicketRepo ticketRepo;
    private WorkTypeClassificatorMapper workTypeClassificatorMapper;
    private WorkTypeClassificatorService workTypeClassificatorService;

    @Transactional
    public void addWorkTypeToTicket(Integer ticketId, TicketDTO ticketDTO) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        if (ticketDTO.workTypeIds() != null) {
            Set<WorkTypeClassificator> workTypes = workTypeClassificatorService
                    .workTypeIdsToWorkTypesSet(ticketDTO.workTypeIds());
            ticket.setWorkTypes(workTypes);
        }
        ticketRepo.save(ticket);
    }

    public List<WorkTypeClassificatorDTO> getTicketWorkTypes(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        return workTypeClassificatorMapper.toDtoList(ticket.getWorkTypes().stream().toList());
    }
}
