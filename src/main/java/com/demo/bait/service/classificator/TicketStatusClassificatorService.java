package com.demo.bait.service.classificator;


import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.TicketStatusClassificatorDTO;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.mapper.classificator.TicketStatusClassificatorMapper;
import com.demo.bait.repository.classificator.TicketStatusClassificatorRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TicketStatusClassificatorService {

    private TicketStatusClassificatorRepo ticketStatusClassificatorRepo;
    private TicketStatusClassificatorMapper ticketStatusClassificatorMapper;

    @Transactional
    public ResponseDTO addTicketStatus(TicketStatusClassificatorDTO ticketStatusClassificatorDTO) {
        TicketStatusClassificator ticketStatus = new TicketStatusClassificator();
        ticketStatus.setStatus(ticketStatusClassificatorDTO.status());
        ticketStatusClassificatorRepo.save(ticketStatus);
        return new ResponseDTO("Ticket status classificator added successfully");
    }

    @Transactional
    public ResponseDTO updateTicketStatus(Integer statusId, TicketStatusClassificatorDTO statusDTO) {
        Optional<TicketStatusClassificator> ticketStatusOpt = ticketStatusClassificatorRepo.findById(statusId);
        if (ticketStatusOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket status classificator with id " + statusId + " not found");
        }
        TicketStatusClassificator ticketStatus = ticketStatusOpt.get();
        if (statusDTO.status() != null) {
            ticketStatus.setStatus(statusDTO.status());
        }
        ticketStatusClassificatorRepo.save(ticketStatus);
        return new ResponseDTO("Ticket status classificator updated successfully");
    }

    public List<TicketStatusClassificatorDTO> getAllTicketStatusClassificators() {
        return ticketStatusClassificatorMapper.toDtoList(ticketStatusClassificatorRepo.findAll());
    }

    public TicketStatusClassificatorDTO getTicketStatusClassificatorById(Integer ticketStatusId) {
        Optional<TicketStatusClassificator> ticketStatusOpt = ticketStatusClassificatorRepo.findById(ticketStatusId);
        if (ticketStatusOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket status classificator with id: " + ticketStatusId + " not found");
        }
        return ticketStatusClassificatorMapper.toDto(ticketStatusOpt.get());
    }
}
