package com.demo.bait.controller;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TicketDTO;
import com.demo.bait.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/ticket")
public class TicketController {

    public final TicketService ticketService;

    @PostMapping("/add")
    public ResponseDTO addTicket(@RequestBody TicketDTO ticketDTO) {
        return ticketService.addTicket(ticketDTO);
    }

    @GetMapping("/client/{clientId}")
    public List<TicketDTO> getTicketsByClientId(@PathVariable Integer clientId) {
        return ticketService.getTicketsByClientId(clientId);
    }

    @GetMapping("/all")
    public List<TicketDTO> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/main/{mainTicketId}")
    public List<TicketDTO> getTicketsByMainTicketId(@PathVariable Integer mainTicketId) {
        return ticketService.getTicketsByMainTicketId(mainTicketId);
    }

    @DeleteMapping("/delete/{ticketId}")
    public ResponseDTO deleteTicket(@PathVariable Integer ticketId) {
        return ticketService.deleteTicket(ticketId);
    }
}
