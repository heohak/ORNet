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

import java.util.List;
import java.util.Optional;

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
        ticketOpt.ifPresent(ticket::setTicket);
        ticketRepo.save(ticket);
        return new ResponseDTO("Ticket added successfully");
    }

    public List<TicketDTO> getTicketsByClientId(Integer clientId) {
        return ticketMapper.toDtoList(ticketRepo.findByClientId(clientId));
    }
}
