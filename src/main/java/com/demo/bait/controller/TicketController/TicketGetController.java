package com.demo.bait.controller.TicketController;

import com.demo.bait.dto.*;
import com.demo.bait.dto.classificator.WorkTypeClassificatorDTO;
import com.demo.bait.service.TicketServices.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/ticket")
public class TicketGetController {

    public final TicketService ticketService;
    public final TicketFileUploadService ticketFileUploadService;
    public final TicketContactsService ticketContactsService;
    public final TicketSpecificationService ticketSpecificationService;
    public final TicketCommentService ticketCommentService;
    public final TicketPaidWorkService ticketPaidWorkService;
    public final TicketWorkTypeService ticketWorkTypeService;

    @GetMapping("/client/{clientId}")
    public List<TicketDTO> getTicketsByClientId(@PathVariable Integer clientId) {
        return ticketService.getTicketsByClientId(clientId);
    }

    @GetMapping("/all")
    public List<TicketDTO> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{ticketId}")
    public TicketDTO getTicketById(@PathVariable Integer ticketId) {
        return ticketService.getTicketById(ticketId);
    }

    @GetMapping("/search")
    public List<TicketDTO> getTickets(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "statusId", required = false) Integer statusId,
            @RequestParam(value = "crisis", required = false) Boolean crisis,
            @RequestParam(value = "paidWork", required = false) Boolean paidWork,
            @RequestParam(value = "workTypeId", required = false) Integer workTypeId,
            @RequestParam(value = "baitWorkerId", required = false) Integer baitWorkerId) {
        return ticketSpecificationService.searchAndFilterTickets(searchTerm, statusId, crisis, paidWork,
                workTypeId, baitWorkerId);
    }

    @GetMapping("/comment/{ticketId}")
    public List<CommentDTO> getTicketComments(@PathVariable Integer ticketId) {
        return ticketCommentService.getTicketComments(ticketId);
    }

    @GetMapping("/files/{ticketId}")
    public List<FileUploadDTO> getTicketFiles(@PathVariable Integer ticketId) {
        return ticketFileUploadService.getTicketFiles(ticketId);
    }

    @GetMapping("/contacts/{ticketId}")
    public List<ClientWorkerDTO> getTicketContacts(@PathVariable Integer ticketId) {
        return ticketContactsService.getTicketContacts(ticketId);
    }

    @GetMapping("/work-types/{ticketId}")
    public List<WorkTypeClassificatorDTO> getTicketWorkTypes(@PathVariable Integer ticketId) {
        return ticketWorkTypeService.getTicketWorkTypes(ticketId);
    }

//    @GetMapping("/paid-work/{ticketId}")
//    public PaidWorkDTO getTicketPaidWork(@PathVariable Integer ticketId) {
//        return ticketPaidWorkService.getTicketPaidWork(ticketId);
//    }
}
