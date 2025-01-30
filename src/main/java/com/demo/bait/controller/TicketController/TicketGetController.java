package com.demo.bait.controller.TicketController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.*;
import com.demo.bait.dto.classificator.WorkTypeClassificatorDTO;
import com.demo.bait.service.TicketServices.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ticket")
public class TicketGetController {

    public final TicketService ticketService;
    public final TicketFileUploadService ticketFileUploadService;
    public final TicketContactsService ticketContactsService;
    public final TicketSpecificationService ticketSpecificationService;
    public final TicketActivityService ticketCommentService;
    public final TicketPaidWorkService ticketPaidWorkService;
    public final TicketWorkTypeService ticketWorkTypeService;
    public final TicketDeviceService ticketDeviceService;
    public final TicketActivityService ticketActivityService;
    private final RequestParamParser requestParamParser;


    @GetMapping("/client/{clientId}")
    public List<TicketDTO> getTicketsByClientId(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "client ID");
        return ticketService.getTicketsByClientId(parsedClientId);
    }

    @GetMapping("/all")
    public List<TicketDTO> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{ticketId}")
    public TicketDTO getTicketById(@PathVariable String ticketId) {
        Integer parsedTicketId = requestParamParser.parseId(ticketId, "ticket ID");
        return ticketService.getTicketById(parsedTicketId);
    }

    @GetMapping("/search")
    public List<TicketDTO> getTickets(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "statusId", required = false) Integer statusId,
            @RequestParam(value = "crisis", required = false) Boolean crisis,
            @RequestParam(value = "paid", required = false) Boolean paidWork,
            @RequestParam(value = "workTypeId", required = false) Integer workTypeId,
            @RequestParam(value = "baitWorkerId", required = false) Integer baitWorkerId) {
        return ticketSpecificationService.searchAndFilterTickets(searchTerm, statusId, crisis, paidWork,
                workTypeId, baitWorkerId);
    }

    @GetMapping("/activity/{ticketId}")
    public List<ActivityDTO> getTicketActivities(@PathVariable String ticketId) {
        Integer parsedTicketId = requestParamParser.parseId(ticketId, "ticket ID");
        return ticketActivityService.getTicketActivities(parsedTicketId);
    }

    @GetMapping("/files/{ticketId}")
    public List<FileUploadDTO> getTicketFiles(@PathVariable String ticketId) {
        Integer parsedTicketId = requestParamParser.parseId(ticketId, "ticket ID");
        return ticketFileUploadService.getTicketFiles(parsedTicketId);
    }

    @GetMapping("/contacts/{ticketId}")
    public List<ClientWorkerDTO> getTicketContacts(@PathVariable String ticketId) {
        Integer parsedTicketId = requestParamParser.parseId(ticketId, "ticket ID");
        return ticketContactsService.getTicketContacts(parsedTicketId);
    }

    @GetMapping("/work-types/{ticketId}")
    public List<WorkTypeClassificatorDTO> getTicketWorkTypes(@PathVariable String ticketId) {
        Integer parsedTicketId = requestParamParser.parseId(ticketId, "ticket ID");
        return ticketWorkTypeService.getTicketWorkTypes(parsedTicketId);
    }

    @GetMapping("/devices/{ticketId}")
    public List<DeviceDTO> getTicketDevices(@PathVariable String ticketId) {
        Integer parsedTicketId = requestParamParser.parseId(ticketId, "ticket ID");
        return ticketDeviceService.getTicketDevices(parsedTicketId);
    }
}
