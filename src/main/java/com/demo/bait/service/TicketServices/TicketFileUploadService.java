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
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        Set<FileUpload> uploadedFiles = fileUploadService.uploadFiles(files);
        ticket.getFiles().addAll(uploadedFiles);
        ticketRepo.save(ticket);
        return new ResponseDTO("Files uploaded successfully to ticket");
    }

    public List<FileUploadDTO> getTicketFiles(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        return fileUploadMapper.toDtoList(ticket.getFiles().stream().toList());
    }
}
