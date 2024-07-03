package com.demo.bait.service;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TicketDTO;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.TicketMapper;
import com.demo.bait.repository.TicketRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TicketService {

    private TicketRepo ticketRepo;
    private TicketMapper ticketMapper;

    public ResponseDTO addTicket(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setClientId(ticketDTO.clientId());
        ticket.setDescription(ticketDTO.description());
        ticketRepo.save(ticket);
        return new ResponseDTO("Ticket added successfully");
    }
}
