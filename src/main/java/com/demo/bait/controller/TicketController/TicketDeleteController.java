package com.demo.bait.controller.TicketController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.TicketServices.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ticket")
public class TicketDeleteController {

    public final TicketService ticketService;

    @DeleteMapping("/delete/{ticketId}")
    public ResponseDTO deleteTicket(@PathVariable Integer ticketId) {
        return ticketService.deleteTicket(ticketId);
    }
}
