package com.demo.bait.controller.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.TicketStatusClassificatorDTO;
import com.demo.bait.service.classificator.TicketStatusClassificatorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/ticket/classificator")
public class TicketStatusClassificatorController {

    public final TicketStatusClassificatorService ticketStatusService;

    @PostMapping("/add")
    public ResponseDTO addTicketStatus(@RequestBody TicketStatusClassificatorDTO ticketStatusClassificatorDTO) {
        return ticketStatusService.addTicketStatus(ticketStatusClassificatorDTO);
    }

    @GetMapping("/all")
    public List<TicketStatusClassificatorDTO> getAllTicketStatuses() {
        return ticketStatusService.getAllTicketStatusClassificators();
    }
}
