package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.repository.MaintenanceRepo;
import com.demo.bait.repository.TicketRepo;
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
public class TicketMaintenanceService {

    private TicketRepo ticketRepo;
    private MaintenanceRepo maintenanceRepo;
    private MaintenanceMapper maintenanceMapper;

    @Transactional
    public ResponseDTO addMaintenanceToTicket(Integer ticketId, Integer maintenanceId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);

        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        if (maintenanceOpt.isEmpty()) {
            throw new EntityNotFoundException("Maintenance with id " + maintenanceId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        Maintenance maintenance = maintenanceOpt.get();
        ticket.getMaintenances().add(maintenance);
        ticketRepo.save(ticket);
        return new ResponseDTO("Maintenance added to ticket successfully");
    }

    public List<MaintenanceDTO> getTicketMaintenances(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        return maintenanceMapper.toDtoList(ticket.getMaintenances().stream().toList());
    }
}
