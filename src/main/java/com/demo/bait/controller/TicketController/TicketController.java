package com.demo.bait.controller.TicketController;

import com.demo.bait.service.TicketServices.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/ticket")
public class TicketController {

    public final TicketService ticketService;
}
