package com.demo.bait.controller.TicketController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TicketDTO;
import com.demo.bait.service.TicketServices.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/ticket")
public class TicketPutController {

    public final TicketService ticketService;
    public final TicketFileUploadService ticketFileUploadService;
    public final TicketMaintenanceService ticketMaintenanceService;
    public final TicketCommentService ticketCommentService;
    public final TicketPaidWorkService ticketPaidWorkService;

//    @PutMapping("/bait/worker/{ticketId}/{workerId}")
//    public ResponseDTO addResponsibleBaitWorkerToTicket(@PathVariable Integer ticketId, @PathVariable Integer workerId) {
//        return ticketService.addResponsibleBaitWorkerToTicket(ticketId, workerId);
//    }

//    @PutMapping("/location/{ticketId}/{locationId}")
//    public ResponseDTO addLocationToTicket(@PathVariable Integer ticketId, @PathVariable Integer locationId) {
//        return ticketService.addLocationToTicket(ticketId, locationId);
//    }

    @PutMapping("/status/{ticketId}/{statusId}")
    public ResponseDTO addStatusToTicket(@PathVariable Integer ticketId, @PathVariable Integer statusId) {
        return ticketService.addStatusToTicket(ticketId, statusId);
    }

//    @PutMapping("/update/{ticketId}")
//    public ResponseDTO updateTicketResponseAndInsideInfo(@PathVariable Integer ticketId,
//                                                         @RequestBody TicketDTO ticketDTO) {
//        return ticketService.updateTicketResponseAndInsideInfo(ticketId, ticketDTO);
//    }

    @PutMapping("/maintenance/{ticketId}/{maintenanceId}")
    public ResponseDTO addMaintenanceToTicket(@PathVariable Integer ticketId, @PathVariable Integer maintenanceId) {
        return ticketMaintenanceService.addMaintenanceToTicket(ticketId, maintenanceId);
    }

//    @PutMapping("/cause/{ticketId}")
//    public ResponseDTO addRootCauseToTicket(@PathVariable Integer ticketId, @RequestBody TicketDTO ticketDTO) {
//        return ticketService.addRootCauseToTicket(ticketId, ticketDTO);
//    }

//    @PutMapping("/end/date/{ticketId}")
//    public ResponseDTO addEndDateTimeToTicket(@PathVariable Integer ticketId, @RequestBody TicketDTO ticketDTO) {
//        return ticketService.addEndDateToTicket(ticketId, ticketDTO);
//    }

//    @PutMapping("/response/date/{ticketId}")
//    public ResponseDTO addResponseDateTimeToTicket(@PathVariable Integer ticketId, @RequestBody TicketDTO ticketDTO) {
//        return ticketService.addResponseDateToTicket(ticketId, ticketDTO);
//    }

//    @PutMapping("/crisis/{ticketId}")
//    public ResponseDTO updateCrisisInTicket(@PathVariable Integer ticketId, @RequestBody TicketDTO ticketDTO) {
//        return ticketService.updateCrisisInTicket(ticketId, ticketDTO);
//    }

//    @PutMapping("/remote/{ticketId}")
//    public ResponseDTO updateRemoteInTicket(@PathVariable Integer ticketId, @RequestBody TicketDTO ticketDTO) {
//        return ticketService.updateRemoteInTicket(ticketId, ticketDTO);
//    }

    @PutMapping("/upload/{ticketId}")
    public ResponseDTO uploadFiles(@PathVariable Integer ticketId, @RequestParam("files") List<MultipartFile> files)
            throws IOException {
        return ticketFileUploadService.uploadFilesToTicket(ticketId, files);
    }

    @PutMapping("/comment/{ticketId}")
    public ResponseDTO addCommentToTicket(@PathVariable Integer ticketId, @RequestParam("comment") String comment) {
        return ticketCommentService.addCommentToTicket(ticketId, comment);
    }

    @PutMapping("/update/whole/{ticketId}")
    public ResponseDTO updateWholeTicket(@PathVariable Integer ticketId, @RequestBody TicketDTO ticketDTO) {
        return ticketService.updateWholeTicket(ticketId, ticketDTO);
    }

    @PutMapping("/add/paid-work/{ticketId}")
    public ResponseDTO addPaidWorkToTicket(@PathVariable Integer ticketId) {
        return ticketPaidWorkService.changeTicketToPaidTicket(ticketId);
    }

    @PutMapping("/add/time/{ticketId}")
    public ResponseDTO addTimeToPaidTicket(@PathVariable Integer ticketId,
                                           @RequestParam(value = "hours", required = false) Integer hours,
                                           @RequestParam(value = "minutes", required = false) Integer minutes) {
        return ticketPaidWorkService.addTimeToTicketPaidWork(ticketId, hours, minutes);
    }

    @PutMapping("/settle/{ticketId}")
    public ResponseDTO settleTicketPaidWork(@PathVariable Integer ticketId) {
        return ticketPaidWorkService.settleTicketPaidWork(ticketId);
    }
}
