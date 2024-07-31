package com.demo.bait.controller.TicketController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TicketDTO;
import com.demo.bait.service.TicketServices.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/ticket")
public class TicketPostController {

    public final TicketService ticketService;

    @PostMapping("/add")
    public ResponseDTO addTicket(@RequestBody TicketDTO ticketDTO) {
        return ticketService.addTicket(ticketDTO);
    }
}
