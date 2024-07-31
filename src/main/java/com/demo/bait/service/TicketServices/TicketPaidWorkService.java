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
    private PaidWorkService paidWorkService;
    private PaidWorkMapper paidWorkMapper;

    @Transactional
    public ResponseDTO changeTicketToPaidTicket(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        if (ticket.getPaidWork() == null) {
            PaidWork paidWork = paidWorkService.createPaidWork();
            ticket.setPaidWork(paidWork);
            ticketRepo.save(ticket);
            return new ResponseDTO("Ticket changed to paid ticket successfully");
        }
        return new ResponseDTO("Ticket is already a paid ticket");
    }

    @Transactional
    public ResponseDTO addTimeToTicketPaidWork(Integer ticketId, Integer hours, Integer minutes) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        PaidWork paidWork = ticket.getPaidWork();
        if (paidWork != null) {
            paidWorkService.addTimeToPaidWork(paidWork.getId(), hours, minutes);
            return new ResponseDTO("Time added to paid ticket successfully");
        }
        return new ResponseDTO("Ticket is not a paid ticket");
    }

    @Transactional
    public ResponseDTO settleTicketPaidWork(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        PaidWork paidWork = ticket.getPaidWork();
        return paidWorkService.setPaidWorkSettled(paidWork.getId());
    }

    public PaidWorkDTO getTicketPaidWork(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        PaidWork paidWork = ticket.getPaidWork();
        if (paidWork == null) {
            throw new IllegalArgumentException("Ticket does not have paid work");
        }
        return paidWorkMapper.toDto(paidWork);
    }
}
