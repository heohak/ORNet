package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TicketDTO;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.ClientWorkerMapper;
import com.demo.bait.repository.ClientWorkerRepo;
import com.demo.bait.repository.TicketRepo;
import com.demo.bait.service.ClientWorkerServices.ClientWorkerService;
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
public class TicketContactsService {

    private TicketRepo ticketRepo;
    private ClientWorkerMapper clientWorkerMapper;
    private ClientWorkerService clientWorkerService;

    @Transactional
    public void addContactsToTicket(Integer ticketId, TicketDTO ticketDTO) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        if (ticketDTO.contactIds() != null) {
            Set<ClientWorker> contacts = clientWorkerService.contactIdsToClientWorkersSet(ticketDTO.contactIds());
            ticket.setContacts(contacts);
        }
        ticketRepo.save(ticket);
    }

    public List<ClientWorkerDTO> getTicketContacts(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        return clientWorkerMapper.toDtoList(ticket.getContacts().stream().toList());
    }
}
