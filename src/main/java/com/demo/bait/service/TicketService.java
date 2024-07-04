package com.demo.bait.service;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TicketDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.TicketMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.TicketRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TicketService {

    private TicketRepo ticketRepo;
    private TicketMapper ticketMapper;
    private ClientRepo clientRepo;

    @Transactional
    public ResponseDTO addTicket(TicketDTO ticketDTO) {
//        Ticket ticket = new Ticket();
//        ticket.setClientId(ticketDTO.clientId());
//        ticket.setDescription(ticketDTO.description());
//        ticketRepo.save(ticket);
//        return new ResponseDTO("Ticket added successfully");
        Optional<Client> clientOpt = clientRepo.findById(ticketDTO.clientId());
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketDTO.mainTicketId());

        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + ticketDTO.clientId() + " not found");
        }

        Ticket ticket = new Ticket();
        ticket.setClient(clientOpt.get());
        ticket.setDescription(ticketDTO.description());
//        ticketOpt.ifPresent(ticket::setTicket);
        if (ticketOpt.isPresent()) {
            if (ticketOpt.get().getTicket() != null) {
                ticket.setTicket(ticketOpt.get().getTicket());
            } else {
                ticket.setTicket(ticketOpt.get());
            }
        }
        ticketRepo.save(ticket);
        return new ResponseDTO("Ticket added successfully");
    }

    public List<TicketDTO> getTicketsByClientId(Integer clientId) {
        return ticketMapper.toDtoList(ticketRepo.findByClientId(clientId));
    }

    public List<TicketDTO> getAllTickets() {
        return ticketMapper.toDtoList(ticketRepo.findAll());
    }

    public List<TicketDTO> getTicketsByMainTicketId(Integer mainTicketId) {
//        return ticketMapper.toDtoList(ticketRepo.findByTicketId(mainTicketId));
        Optional<Ticket> mainTicketOpt = ticketRepo.findById(mainTicketId);

        if (mainTicketOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Ticket mainTicket = mainTicketOpt.get();
        List<Ticket> ticketList = ticketRepo.findByTicketId(mainTicketId);
        ticketList.add(mainTicket);

        List<Ticket> sortedTickets = ticketList.stream().sorted(Comparator.comparing(Ticket::getId)).toList();

        return ticketMapper.toDtoList(sortedTickets);
    }
}
