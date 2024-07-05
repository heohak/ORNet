package com.demo.bait.controller;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TicketDTO;
import com.demo.bait.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class TicketController {

    public final TicketService ticketService;

    @PostMapping("/ticket")
    public ResponseDTO addTicket(@RequestBody TicketDTO ticketDTO) {
        return ticketService.addTicket(ticketDTO);
    }

    @GetMapping("/tickets/{clientId}")
    public List<TicketDTO> getTicketsByClientId(@PathVariable Integer clientId) {
        return ticketService.getTicketsByClientId(clientId);
    }

    @GetMapping("/tickets")
    public List<TicketDTO> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/tickets/main/{mainTicketId}")
    public List<TicketDTO> getTicketsByMainTicketId(@PathVariable Integer mainTicketId) {
        return ticketService.getTicketsByMainTicketId(mainTicketId);
    }

    @DeleteMapping("/ticket/{ticketId}")
    public ResponseDTO deleteTicket(@PathVariable Integer ticketId) {
        return ticketService.deleteTicket(ticketId);
    }
}
