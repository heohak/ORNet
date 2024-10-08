package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.PaidWorkDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.PaidWork;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.PaidWorkMapper;
import com.demo.bait.repository.TicketRepo;
import com.demo.bait.service.PaidWorkServices.PaidWorkService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TicketPaidWorkService {

    private TicketRepo ticketRepo;

    @Transactional
    public ResponseDTO changeTicketToPaidTicket(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        if (ticket.getPaid() == null || !ticket.getPaid()) {
            ticket.setPaid(true);
            ticket.setSettled(false);
            return new ResponseDTO("Ticket changed to paid ticket successfully");
        }
        return new ResponseDTO("Ticket is already a paid ticket");
    }

    @Transactional
    public ResponseDTO changeTicketToNotPaidTicket(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        if (Boolean.TRUE.equals(ticket.getPaid())) {
            ticket.setPaid(false);
            ticket.setSettled(false);
            return new ResponseDTO("Ticket changed to not paid ticket successfully");
        }
        return new ResponseDTO("Ticket is already not paid");
    }

    @Transactional
    public ResponseDTO settleTicketPaidWork(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        if (ticket.getPaid() && !ticket.getSettled()) {
            ticket.setSettled(true);
            return new ResponseDTO("Ticket settled successfully");
        }
        return new ResponseDTO("Failed to settle, ticket is already settled or ticket is not paid");
    }
}
