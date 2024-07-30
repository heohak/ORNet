package com.demo.bait.controller;

import com.demo.bait.dto.*;
import com.demo.bait.dto.classificator.WorkTypeClassificatorDTO;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

//    @GetMapping("/search/{statusId}")
//    public List<TicketDTO> searchAndFilterTickets(@RequestParam("q") String query, @PathVariable Integer statusId) {
//        return ticketService.searchAndFilterTickets(query, statusId);
//    }
//
//    @GetMapping("/search")
//    public List<TicketDTO> searchTickets(@RequestParam("q") String query) {
//        return ticketService.searchTickets(query);
//    }
//
//    @GetMapping("/search/crisis/{statusId}")
//    public List<TicketDTO> searchAndFilterCrisisTickets(@RequestParam("q") String query, @PathVariable Integer statusId,
//                                                        @RequestParam("crisis") Boolean crisis) {
//        return ticketService.searchAndFilterCrisisTickets(query, statusId, crisis);
//    }

    @GetMapping("/search")
    public List<TicketDTO> getTickets(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "statusId", required = false) Integer statusId,
            @RequestParam(value = "crisis", required = false) Boolean crisis,
            @RequestParam(value = "paidWork", required = false) Boolean paidWork,
            @RequestParam(value = "workTypeId", required = false) Integer workTypeId) {
        return ticketService.searchAndFilterTickets(searchTerm, statusId, crisis, paidWork, workTypeId);
    }

    @PutMapping("/maintenance/{ticketId}/{maintenanceId}")
    public ResponseDTO addMaintenanceToTicket(@PathVariable Integer ticketId, @PathVariable Integer maintenanceId) {
        return ticketService.addMaintenanceToTicket(ticketId, maintenanceId);
    }

    @GetMapping("/maintenance/{ticketId}")
    public List<MaintenanceDTO> getTicketMaintenances(@PathVariable Integer ticketId) {
        return ticketService.getTicketMaintenances(ticketId);
    }

    @PutMapping("/cause/{ticketId}")
    public ResponseDTO addRootCauseToTicket(@PathVariable Integer ticketId, @RequestBody TicketDTO ticketDTO) {
        return ticketService.addRootCauseToTicket(ticketId, ticketDTO);
    }

    @PutMapping("/end/date/{ticketId}")
    public ResponseDTO addEndDateTimeToTicket(@PathVariable Integer ticketId, @RequestBody TicketDTO ticketDTO) {
        return ticketService.addEndDateToTicket(ticketId, ticketDTO);
    }

    @PutMapping("/response/date/{ticketId}")
    public ResponseDTO addResponseDateTimeToTicket(@PathVariable Integer ticketId, @RequestBody TicketDTO ticketDTO) {
        return ticketService.addResponseDateToTicket(ticketId, ticketDTO);
    }

    @PutMapping("/crisis/{ticketId}")
    public ResponseDTO updateCrisisInTicket(@PathVariable Integer ticketId, @RequestBody TicketDTO ticketDTO) {
        return ticketService.updateCrisisInTicket(ticketId, ticketDTO);
    }

    @PutMapping("/remote/{ticketId}")
    public ResponseDTO updateRemoteInTicket(@PathVariable Integer ticketId, @RequestBody TicketDTO ticketDTO) {
        return ticketService.updateRemoteInTicket(ticketId, ticketDTO);
    }

    @PutMapping("/upload/{ticketId}")
    public ResponseDTO uploadFiles(@PathVariable Integer ticketId, @RequestParam("files") List<MultipartFile> files)
            throws IOException {
        return ticketService.uploadFilesToTicket(ticketId, files);
    }

    @PutMapping("/comment/{ticketId}")
    public ResponseDTO addCommentToTicket(@PathVariable Integer ticketId, @RequestParam("comment") String comment) {
        return ticketService.addCommentToTicket(ticketId, comment);
    }

    @GetMapping("/comment/{ticketId}")
    public List<CommentDTO> getTicketComments(@PathVariable Integer ticketId) {
        return ticketService.getTicketComments(ticketId);
    }

    @PutMapping("/update/whole/{ticketId}")
    public ResponseDTO updateWholeTicket(@PathVariable Integer ticketId, @RequestBody TicketDTO ticketDTO) {
        return ticketService.updateWholeTicket(ticketId, ticketDTO);
    }

    @GetMapping("/files/{ticketId}")
    public List<FileUploadDTO> getTicketFiles(@PathVariable Integer ticketId) {
        return ticketService.getTicketFiles(ticketId);
    }

    @GetMapping("/contacts/{ticketId}")
    public List<ClientWorkerDTO> getTicketContacts(@PathVariable Integer ticketId) {
        return ticketService.getTicketContacts(ticketId);
    }

    @GetMapping("/work-types/{ticketId}")
    public List<WorkTypeClassificatorDTO> getTicketWorkTypes(@PathVariable Integer ticketId) {
        return ticketService.getTicketWorkTypes(ticketId);
    }

    @PutMapping("/add/paid-work/{ticketId}")
    public ResponseDTO addPaidWorkToTicket(@PathVariable Integer ticketId) {
        return ticketService.changeTicketToPaidTicket(ticketId);
    }

    @PutMapping("/add/time/{ticketId}")
    public ResponseDTO addTimeToPaidTicket(@PathVariable Integer ticketId,
                                           @RequestParam(value = "hours", required = false) Integer hours,
                                           @RequestParam(value = "minutes", required = false) Integer minutes) {
        return ticketService.addTimeToTicketPaidWork(ticketId, hours, minutes);
    }

    @GetMapping("/paid-work/{ticketId}")
    public PaidWorkDTO getTicketPaidWork(@PathVariable Integer ticketId) {
        return ticketService.getTicketPaidWork(ticketId);
    }

    @PutMapping("/settle/{ticketId}")
    public ResponseDTO settleTicketPaidWork(@PathVariable Integer ticketId) {
        return ticketService.settleTicketPaidWork(ticketId);
    }
}
