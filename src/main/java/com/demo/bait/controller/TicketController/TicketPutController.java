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
    public final TicketActivityService ticketCommentService;
    public final TicketPaidWorkService ticketPaidWorkService;

    @PutMapping("/status/{ticketId}/{statusId}")
    public ResponseDTO addStatusToTicket(@PathVariable Integer ticketId, @PathVariable Integer statusId) {
        return ticketService.addStatusToTicket(ticketId, statusId);
    }

    @PutMapping("/upload/{ticketId}")
    public ResponseDTO uploadFiles(@PathVariable Integer ticketId, @RequestParam("files") List<MultipartFile> files)
            throws IOException {
        return ticketFileUploadService.uploadFilesToTicket(ticketId, files);
    }

    @PutMapping("/activity/{ticketId}")
    public ResponseDTO addActivityToTicket(@PathVariable Integer ticketId,
                                          @RequestParam("activity") String activity,
                                          @RequestParam(value = "hours", required = false) Integer hours,
                                          @RequestParam(value = "minutes", required = false) Integer minutes,
                                          @RequestParam(value = "paid", required = false) Boolean paid) {
        return ticketCommentService.addActivityToTicket(ticketId, activity, hours, minutes, paid);
    }

    @PutMapping("/update/whole/{ticketId}")
    public ResponseDTO updateWholeTicket(@PathVariable Integer ticketId, @RequestBody TicketDTO ticketDTO) {
        return ticketService.updateWholeTicket(ticketId, ticketDTO);
    }

    @PutMapping("/add/paid/{ticketId}")
    public ResponseDTO changeTicketToPaid(@PathVariable Integer ticketId) {
        return ticketPaidWorkService.changeTicketToPaidTicket(ticketId);
    }

//    @PutMapping("/add/time/{ticketId}")
//    public ResponseDTO addTimeToPaidTicket(@PathVariable Integer ticketId,
//                                           @RequestParam(value = "hours", required = false) Integer hours,
//                                           @RequestParam(value = "minutes", required = false) Integer minutes) {
//        return ticketPaidWorkService.addTimeToTicketPaidWork(ticketId, hours, minutes);
//    }

    @PutMapping("/settle/{ticketId}")
    public ResponseDTO settleTicketPaidWork(@PathVariable Integer ticketId) {
        return ticketPaidWorkService.settleTicketPaidWork(ticketId);
    }

    @PutMapping("/remove/paid/{ticketId}")
    public ResponseDTO changeTicketToNotPaid(@PathVariable Integer ticketId) {
        return ticketPaidWorkService.changeTicketToNotPaidTicket(ticketId);
    }
}
