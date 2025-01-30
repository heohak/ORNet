package com.demo.bait.controller.classificator;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.TicketStatusClassificatorDTO;
import com.demo.bait.service.classificator.TicketStatusClassificatorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ticket/classificator")
public class TicketStatusClassificatorController {

    public final TicketStatusClassificatorService ticketStatusService;
    private RequestParamParser requestParamParser;


    @PostMapping("/add")
    public ResponseDTO addTicketStatus(@RequestBody TicketStatusClassificatorDTO ticketStatusClassificatorDTO) {
        return ticketStatusService.addTicketStatus(ticketStatusClassificatorDTO);
    }

    @GetMapping("/all")
    public List<TicketStatusClassificatorDTO> getAllTicketStatuses() {
        return ticketStatusService.getAllTicketStatusClassificators();
    }

    @GetMapping("/{statusId}")
    public TicketStatusClassificatorDTO getTicketStatusClassificatorById(@PathVariable String statusId) {
        Integer parsedStatusId = requestParamParser.parseId(statusId, "statusId");
        return ticketStatusService.getTicketStatusClassificatorById(parsedStatusId);
    }

    @PutMapping("/update/{statusId}")
    public ResponseDTO updateTicketStatusClassificator(@PathVariable Integer statusId,
                                                       @RequestBody TicketStatusClassificatorDTO statusDTO) {
        return ticketStatusService.updateTicketStatus(statusId, statusDTO);
    }

    @DeleteMapping("/{statusId}")
    public ResponseDTO deleteTicketStatus(@PathVariable Integer statusId) {
        return ticketStatusService.deleteTicketStatus(statusId);
    }

    @GetMapping("/history/{statusId}")
    public List<TicketStatusClassificatorDTO> getTicketStatusClassificatorHistory(@PathVariable String statusId) {
        Integer parsedStatusId = requestParamParser.parseId(statusId, "statusId");
        return ticketStatusService.getTicketStatusHistory(parsedStatusId);
    }

    @GetMapping("/deleted")
    public List<TicketStatusClassificatorDTO> getDeletedTicketStatuses() {
        return ticketStatusService.getDeletedTicketStatuses();
    }
}
