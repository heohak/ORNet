package com.demo.bait.service;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TicketDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.TicketMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.TicketRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TicketService {

    private TicketRepo ticketRepo;
    private TicketMapper ticketMapper;
    private ClientRepo clientRepo;

    @Transactional
    public ResponseDTO addTicket(TicketDTO ticketDTO) {
//        Ticket ticket = new Ticket();
//        ticket.setClientId(ticketDTO.clientId());
//        ticket.setDescription(ticketDTO.description());
//        ticketRepo.save(ticket);
//        return new ResponseDTO("Ticket added successfully");
        Optional<Client> clientOpt = clientRepo.findById(ticketDTO.clientId());

        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + ticketDTO.clientId() + " not found");
        }

        Ticket ticket = new Ticket();
        ticket.setClient(clientOpt.get());
        ticket.setDescription(ticketDTO.description());

        if (ticketDTO.mainTicketId() != null) {
            Optional<Ticket> ticketOpt = ticketRepo.findById(ticketDTO.mainTicketId());

            if (ticketOpt.isPresent()) {
                Ticket mainTicket = ticketOpt.get();
                if (mainTicket.getClient().getId().equals(clientOpt.get().getId())) {
                    ticket.setTicket(mainTicket.getTicket() != null ? mainTicket.getTicket() : mainTicket);
                }
            }
        }
        ticketRepo.save(ticket);
        return new ResponseDTO("Ticket added successfully");
    }

    public List<TicketDTO> getTicketsByClientId(Integer clientId) {
        return ticketMapper.toDtoList(ticketRepo.findByClientId(clientId));
    }

    public List<TicketDTO> getAllTickets() {
        return ticketMapper.toDtoList(ticketRepo.findAll());
    }

    public List<TicketDTO> getTicketsByMainTicketId(Integer mainTicketId) {
//        Optional<Ticket> mainTicketOpt = ticketRepo.findById(mainTicketId);
//
//        if (mainTicketOpt.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        Ticket mainTicket = mainTicketOpt.get();
//        List<Ticket> ticketList = new ArrayList<>();
//
//        if (mainTicket.getTicket() != null) {
//            Integer actualMainTicketId = mainTicket.getTicket().getId();
//            Ticket actualMainTicket = ticketRepo.getReferenceById(actualMainTicketId);
//            ticketList.addAll(ticketRepo.findByTicketId(actualMainTicketId));
//            ticketList.add(actualMainTicket);
//        } else {
//            ticketList.addAll(ticketRepo.findByTicketId(mainTicketId));
//            ticketList.add(mainTicket);
//        }
//
//        List<Ticket> sortedTickets = ticketList.stream().sorted(Comparator.comparing(Ticket::getId)).toList();
//
//        return ticketMapper.toDtoList(sortedTickets);

        return ticketRepo.findById(mainTicketId)
                .map(mainTicket -> {
                    List<Ticket> ticketList = new ArrayList<>();

                    Ticket rootTicket = mainTicket.getTicket() != null ? mainTicket.getTicket() : mainTicket;
                    ticketList.addAll(ticketRepo.findByTicketId(rootTicket.getId()));
                    ticketList.add(rootTicket);

                    return ticketList.stream()
                            .sorted(Comparator.comparing(Ticket::getId))
                            .collect(Collectors.toList());
                })
                .map(ticketMapper::toDtoList)
                .orElse(Collections.emptyList());
    }

    public ResponseDTO deleteTicket(Integer ticketId) {
        ticketRepo.deleteById(ticketId);
        return new ResponseDTO("Ticket deleted successfully");
    }
}
