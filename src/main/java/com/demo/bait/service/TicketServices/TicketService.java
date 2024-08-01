package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.*;
import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.*;
import com.demo.bait.mapper.classificator.WorkTypeClassificatorMapper;
import com.demo.bait.repository.*;
import com.demo.bait.repository.classificator.TicketStatusClassificatorRepo;
import com.demo.bait.repository.classificator.WorkTypeClassificatorRepo;
import com.demo.bait.service.ClientWorkerServices.ClientWorkerService;
import com.demo.bait.service.CommentServices.CommentService;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import com.demo.bait.service.classificator.WorkTypeClassificatorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TicketService {

    private TicketRepo ticketRepo;
    private TicketMapper ticketMapper;
    private ClientRepo clientRepo;
    private LocationRepo locationRepo;
    private TicketStatusClassificatorRepo ticketStatusRepo;
    private BaitWorkerRepo baitWorkerRepo;
    private TicketContactsService ticketContactsService;
    private TicketWorkTypeService ticketWorkTypeService;
    private MaintenanceService maintenanceService;
    private FileUploadService fileUploadService;
    private CommentService commentService;
    private WorkTypeClassificatorService workTypeClassificatorService;
    private ClientWorkerService clientWorkerService;


    @Transactional
    public ResponseDTO addTicket(TicketDTO ticketDTO) {
        Optional<Client> clientOpt = clientRepo.findById(ticketDTO.clientId());

        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + ticketDTO.clientId() + " not found");
        }

        Ticket ticket = new Ticket();
        ticket.setClient(clientOpt.get());
        ticket.setTitle(ticketDTO.title());
        ticket.setBaitNumeration(ticketDTO.baitNumeration());
        ticket.setClientNumeration(ticketDTO.clientNumeration());
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

        ticket.setStartDateTime(ticketDTO.startDateTime());

        if (ticketDTO.locationId() != null && locationRepo.findById(ticketDTO.locationId()).isPresent()) {
            ticket.setLocation(locationRepo.getReferenceById(ticketDTO.locationId()));
        }

        if (ticketDTO.contactIds() != null) {
            Set<ClientWorker> contacts = clientWorkerService.contactIdsToClientWorkersSet(ticketDTO.contactIds());
            ticket.setContacts(contacts);
        }

        if (ticketDTO.workTypeIds() != null) {
            Set<WorkTypeClassificator> workTypes = workTypeClassificatorService
                    .workTypeIdsToWorkTypesSet(ticketDTO.workTypeIds());
            ticket.setWorkTypes(workTypes);
        }

        if (ticketDTO.remote() == null) {
            ticket.setRemote(false);
        } else {
            ticket.setRemote(ticketDTO.remote());
        }

        if (ticketDTO.crisis() == null) {
            ticket.setCrisis(false);
        } else {
            ticket.setCrisis(ticketDTO.crisis());
        }

        if (ticketDTO.statusId() != null && ticketStatusRepo.findById(ticketDTO.statusId()).isPresent()) {
            ticket.setStatus(ticketStatusRepo.getReferenceById(ticketDTO.statusId()));
        }

        if (ticketDTO.baitWorkerId() != null && baitWorkerRepo.findById(ticketDTO.baitWorkerId()).isPresent()) {
            ticket.setBaitWorker(baitWorkerRepo.getReferenceById(ticketDTO.baitWorkerId()));
        }

        ticket.setResponseDateTime(ticketDTO.responseDateTime());
        ticket.setResponse(ticketDTO.response());
        ticket.setInsideInfo(ticketDTO.insideInfo());
        ticket.setEndDateTime(ticketDTO.endDateTime());
        ticket.setRootCause(ticketDTO.rootCause());

        if (ticketDTO.commentIds() != null) {
            Set<Comment> comments = commentService.commentIdsToCommentsSet(ticketDTO.commentIds());
            ticket.setComments(comments);
        }

        if (ticketDTO.maintenanceIds() != null) {
            Set<Maintenance> maintenances = maintenanceService.maintenanceIdsToMaintenancesSet(ticketDTO.maintenanceIds());
            ticket.setMaintenances(maintenances);
        }

        if (ticketDTO.fileIds() != null) {
            Set<FileUpload> files = fileUploadService.fileIdsToFilesSet(ticketDTO.fileIds());
            ticket.setFiles(files);
        }

        ticketRepo.save(ticket);
        setTicketUpdateTime(ticket.getId());
        return new ResponseDTO("Ticket added successfully");
    }

    @Transactional
    public ResponseDTO deleteTicket(Integer ticketId) {
        ticketRepo.deleteById(ticketId);
        return new ResponseDTO("Ticket deleted successfully");
    }

    @Transactional
    public ResponseDTO addResponsibleBaitWorkerToTicket(Integer ticketId, Integer baitWorkerId) {
        if (baitWorkerId != null) {
            Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
            Optional<BaitWorker> baitWorkerOpt = baitWorkerRepo.findById(baitWorkerId);

            if (ticketOpt.isEmpty()) {
                throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
            }
            if (baitWorkerOpt.isEmpty()) {
                throw new EntityNotFoundException("Bait worker with id " + baitWorkerId + " not found");
            }

            Ticket ticket = ticketOpt.get();
            BaitWorker baitWorker = baitWorkerOpt.get();
            ticket.setBaitWorker(baitWorker);
            ticketRepo.save(ticket);
            return new ResponseDTO("Responsible bait worker added to ticket");
        }
        return new ResponseDTO("No responsible worker added");
    }

    @Transactional
    public ResponseDTO addLocationToTicket(Integer ticketId, Integer locationId) {
        if (locationId != null) {
            Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
            Optional<Location> locationOpt = locationRepo.findById(locationId);

            if (ticketOpt.isEmpty()) {
                throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
            }
            if (locationOpt.isEmpty()) {
                throw new EntityNotFoundException("Location with id " + locationId + " not found");
            }

            Ticket ticket = ticketOpt.get();
            Location location = locationOpt.get();
            ticket.setLocation(location);
            ticketRepo.save(ticket);
            return new ResponseDTO("Location added to ticket successfully");
        }
        return new ResponseDTO("No location added");
    }

    @Transactional
    public ResponseDTO addStatusToTicket(Integer ticketId, Integer statusId) {
        if (statusId != null) {
            Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
            Optional<TicketStatusClassificator> statusOpt = ticketStatusRepo.findById(statusId);

            if (ticketOpt.isEmpty()) {
                throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
            }
            if (statusOpt.isEmpty()) {
                throw new EntityNotFoundException("Ticket status with id " + statusId + " not found");
            }

            Ticket ticket = ticketOpt.get();
            TicketStatusClassificator status = statusOpt.get();
            ticket.setStatus(status);
            ticketRepo.save(ticket);
            return new ResponseDTO("Status added to ticket successfully");
        }
        return new ResponseDTO("No status added");
    }

    @Transactional
    public void addClientToTicket(Integer ticketId, Integer clientId) {
        if (clientId != null) {
            Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
            Optional<Client> clientOpt = clientRepo.findById(clientId);

            if (ticketOpt.isEmpty()) {
                throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
            }
            if (clientOpt.isEmpty()) {
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }
            Ticket ticket = ticketOpt.get();
            Client client = clientOpt.get();
            ticket.setClient(client);
            ticketRepo.save(ticket);
        }
    }

    @Transactional
    public ResponseDTO updateTicketResponseAndInsideInfo(Integer ticketId, TicketDTO ticketDTO) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        ticket.setResponse(ticketDTO.response());
        ticket.setInsideInfo(ticketDTO.insideInfo());
        ticketRepo.save(ticket);
        return new ResponseDTO("Ticket response and inside info updated successfully");
    }

    @Transactional
    public void updateTicketDescription(Integer ticketId, TicketDTO ticketDTO) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        ticket.setDescription(ticketDTO.description());
        ticketRepo.save(ticket);
    }

    @Transactional
    public ResponseDTO addRootCauseToTicket(Integer ticketId, TicketDTO ticketDTO) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        ticket.setRootCause(ticketDTO.rootCause());
        ticketRepo.save(ticket);
        return new ResponseDTO("Root Cause added to ticket");
    }

    @Transactional
    public ResponseDTO addEndDateToTicket(Integer ticketId, TicketDTO ticketDTO) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        ticket.setEndDateTime(ticketDTO.endDateTime());
        ticketRepo.save(ticket);
        return new ResponseDTO("End Date added to ticket");
    }

    @Transactional
    public ResponseDTO addResponseDateToTicket(Integer ticketId, TicketDTO ticketDTO) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        ticket.setResponseDateTime(ticketDTO.responseDateTime());
        ticketRepo.save(ticket);
        return new ResponseDTO("Response Date added to ticket");
    }

    @Transactional
    public ResponseDTO updateCrisisInTicket(Integer ticketId, TicketDTO ticketDTO) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        ticket.setCrisis(ticketDTO.crisis());
        ticketRepo.save(ticket);
        return new ResponseDTO("Crisis updated successfully");
    }

    @Transactional
    public ResponseDTO updateRemoteInTicket(Integer ticketId, TicketDTO ticketDTO) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        ticket.setRemote(ticketDTO.remote());
        ticketRepo.save(ticket);
        return new ResponseDTO("Remote updated successfully");
    }

    @Transactional
    public void setTicketUpdateTime(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        ticket.setUpdateDateTime(LocalDateTime.now().withNano(0));
        ticketRepo.save(ticket);
    }

    @Transactional
    public ResponseDTO updateWholeTicket(Integer ticketId, TicketDTO ticketDTO) {
        addResponseDateToTicket(ticketId, ticketDTO);
        // endDateTime??
        // updateDateTime??
        updateCrisisInTicket(ticketId, ticketDTO);
        updateRemoteInTicket(ticketId, ticketDTO);
        ticketWorkTypeService.addWorkTypeToTicket(ticketId, ticketDTO);
        addResponsibleBaitWorkerToTicket(ticketId, ticketDTO.baitWorkerId());
        addClientToTicket(ticketId, ticketDTO.clientId());
        addLocationToTicket(ticketId, ticketDTO.locationId());
        addStatusToTicket(ticketId, ticketDTO.statusId());
        addRootCauseToTicket(ticketId, ticketDTO);
        updateTicketDescription(ticketId, ticketDTO);
        updateTicketResponseAndInsideInfo(ticketId, ticketDTO);
        ticketContactsService.addContactsToTicket(ticketId, ticketDTO);
        setTicketUpdateTime(ticketId);
        return new ResponseDTO("Whole ticket updated successfully");
    }

    public List<TicketDTO> getTicketsByClientId(Integer clientId) {
        return ticketMapper.toDtoList(ticketRepo.findByClientId(clientId));
    }

    public List<TicketDTO> getAllTickets() {
        return ticketMapper.toDtoList(ticketRepo.findAll());
    }

    public List<TicketDTO> getTicketsByMainTicketId(Integer mainTicketId) {
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

//    public List<TicketDTO> getTicketsByMainTicketId(Integer mainTicketId) {
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
//    }
}
