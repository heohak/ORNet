package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.FileUploadMapper;
import com.demo.bait.repository.FileUploadRepo;
import com.demo.bait.repository.TicketRepo;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class TicketFileUploadService {

    private TicketRepo ticketRepo;
    private FileUploadService fileUploadService;
    private FileUploadMapper fileUploadMapper;

    @Transactional
    public ResponseDTO uploadFilesToTicket(Integer ticketId, List<MultipartFile> files) throws IOException {
        log.info("Starting file upload to ticket with ID: {}", ticketId);

        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            log.error("Ticket with ID: {} not found", ticketId);
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        log.debug("Uploading {} files to ticket with ID: {}", files.size(), ticketId);
        Set<FileUpload> uploadedFiles = fileUploadService.uploadFiles(files);

        ticket.getFiles().addAll(uploadedFiles);
        ticketRepo.save(ticket);

        log.info("Files uploaded successfully to ticket with ID: {}. Total files uploaded: {}", ticketId, uploadedFiles.size());
        return new ResponseDTO("Files uploaded successfully to ticket");
    }

    public List<FileUploadDTO> getTicketFiles(Integer ticketId) {
        if (ticketId == null) {
            log.warn("Ticket ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        log.info("Fetching files for ticket with ID: {}", ticketId);

        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            log.error("Ticket with ID: {} not found", ticketId);
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        List<FileUploadDTO> files = fileUploadMapper.toDtoList(ticket.getFiles().stream().toList());
        log.info("Found {} files for ticket with ID: {}", files.size(), ticketId);
        return files;
    }
}
