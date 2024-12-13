package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Ticket;
import com.demo.bait.repository.TicketRepo;
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
        log.info("Changing ticket with ID: {} to a paid ticket", ticketId);

        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            log.error("Ticket with ID: {} not found", ticketId);
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        if (ticket.getPaid() == null || !ticket.getPaid()) {
            log.debug("Ticket with ID: {} is not paid. Changing to paid.", ticketId);
            ticket.setPaid(true);
            ticket.setSettled(false);
            log.info("Ticket with ID: {} successfully changed to paid ticket", ticketId);
            return new ResponseDTO("Ticket changed to paid ticket successfully");
        }

        log.warn("Ticket with ID: {} is already a paid ticket", ticketId);
        return new ResponseDTO("Ticket is already a paid ticket");
    }

    @Transactional
    public ResponseDTO changeTicketToNotPaidTicket(Integer ticketId) {
        log.info("Changing ticket with ID: {} to a not paid ticket", ticketId);

        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            log.error("Ticket with ID: {} not found", ticketId);
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        if (Boolean.TRUE.equals(ticket.getPaid())) {
            log.debug("Ticket with ID: {} is paid. Changing to not paid.", ticketId);
            ticket.setPaid(false);
            ticket.setSettled(false);
            log.info("Ticket with ID: {} successfully changed to not paid ticket", ticketId);
            return new ResponseDTO("Ticket changed to not paid ticket successfully");
        }

        log.warn("Ticket with ID: {} is already not paid", ticketId);
        return new ResponseDTO("Ticket is already not paid");
    }

    @Transactional
    public ResponseDTO settleTicketPaidWork(Integer ticketId) {
        log.info("Settling paid work for ticket with ID: {}", ticketId);

        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            log.error("Ticket with ID: {} not found", ticketId);
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        if (ticket.getPaid() && !ticket.getSettled()) {
            log.debug("Ticket with ID: {} is paid and not settled. Settling now.", ticketId);
            ticket.setSettled(true);
            log.info("Ticket with ID: {} successfully settled", ticketId);
            return new ResponseDTO("Ticket settled successfully");
        }

        log.warn("Failed to settle ticket with ID: {}. Either already settled or not paid.", ticketId);
        return new ResponseDTO("Failed to settle, ticket is already settled or ticket is not paid");
    }
}
