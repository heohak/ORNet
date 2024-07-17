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

    @PutMapping("/bait/worker/{ticketId}/{workerId}")
    public ResponseDTO addResponsibleBaitWorkerToTicket(@PathVariable Integer ticketId, @PathVariable Integer workerId) {
        return ticketService.addResponsibleBaitWorkerToTicket(ticketId, workerId);
    }

    @PutMapping("/location/{ticketId}/{locationId}")
    public ResponseDTO addLocationToTicket(@PathVariable Integer ticketId, @PathVariable Integer locationId) {
        return ticketService.addLocationToTicket(ticketId, locationId);
    }

    @PutMapping("/status/{ticketId}/{statusId}")
    public ResponseDTO addStatusToTicket(@PathVariable Integer ticketId, @PathVariable Integer statusId) {
        return ticketService.addStatusToTicket(ticketId, statusId);
    }

    @PutMapping("/contact/{ticketId}/{workerId}")
    public ResponseDTO addContactToTicket(@PathVariable Integer ticketId, @PathVariable Integer workerId) {
        return ticketService.addContactToTicket(ticketId, workerId);
    }

    @PutMapping("/update/{ticketId}")
    public ResponseDTO updateTicketResponseAndInsideInfo(@PathVariable Integer ticketId,
                                                         @RequestBody TicketDTO ticketDTO) {
        return ticketService.updateTicketResponseAndInsideInfo(ticketId, ticketDTO);
    }

    @GetMapping("/status/{statusId}")
    public List<TicketDTO> getTicketsByStatusId(@PathVariable Integer statusId) {
        return ticketService.getTicketsByStatusId(statusId);
    }

    @GetMapping("/search")
    public List<TicketDTO> searchTickets(@RequestParam("q") String query) {
        return ticketService.searchTickets(query);
    }
}
