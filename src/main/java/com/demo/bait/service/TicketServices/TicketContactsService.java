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
    public void addContactsToTicket(Ticket ticket, TicketDTO ticketDTO) {
        log.info("Adding contacts to ticket with ID: {}", ticket.getId());
        if (ticketDTO.contactIds() != null) {
            log.debug("Contact IDs provided: {}", ticketDTO.contactIds());
            Set<ClientWorker> contacts = clientWorkerService.contactIdsToClientWorkersSet(ticketDTO.contactIds());
            ticket.setContacts(contacts);
            ticketRepo.save(ticket);
            log.info("Contacts successfully added to ticket with ID: {}", ticket.getId());
        } else {
            log.warn("No contact IDs provided for ticket with ID: {}", ticket.getId());
        }
    }

    public List<ClientWorkerDTO> getTicketContacts(Integer ticketId) {
        log.info("Fetching contacts for ticket with ID: {}", ticketId);
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            log.error("Ticket with ID: {} not found", ticketId);
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        List<ClientWorkerDTO> contacts = clientWorkerMapper.toDtoList(ticket.getContacts().stream().toList());
        log.info("Found {} contacts for ticket with ID: {}", contacts.size(), ticketId);
        return contacts;
    }
}
