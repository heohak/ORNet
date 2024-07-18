package com.demo.bait.service;

import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TicketDTO;
import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.mapper.TicketMapper;
import com.demo.bait.repository.*;
import com.demo.bait.repository.classificator.TicketStatusClassificatorRepo;
import com.demo.bait.specification.TicketSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private ClientWorkerRepo clientWorkerRepo;
    private TicketStatusClassificatorRepo ticketStatusRepo;
    private BaitWorkerRepo baitWorkerRepo;
    private MaintenanceRepo maintenanceRepo;
    private MaintenanceMapper maintenanceMapper;
    private FileUploadRepo fileUploadRepo;
    private FileUploadService fileUploadService;


    @Transactional
    public ResponseDTO addTicket(TicketDTO ticketDTO) {
        Optional<Client> clientOpt = clientRepo.findById(ticketDTO.clientId());

        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + ticketDTO.clientId() + " not found");
        }

        Ticket ticket = new Ticket();
        ticket.setClient(clientOpt.get());
        ticket.setTitle(ticketDTO.title());
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
            Set<ClientWorker> contacts = new HashSet<>();
            for (Integer id : ticketDTO.contactIds()) {
                ClientWorker contact = clientWorkerRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid worker ID: " + id));
                contacts.add(contact);
            }
            ticket.setContacts(contacts);
        }

        ticket.setWorkType(ticketDTO.workType());

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

        if (ticketDTO.maintenanceIds() != null) {
            Set<Maintenance> maintenances = new HashSet<>();
            for (Integer id : ticketDTO.maintenanceIds()) {
                Maintenance maintenance = maintenanceRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance ID: " + id));
                maintenances.add(maintenance);
            }
            ticket.setMaintenances(maintenances);
        }

        if (ticketDTO.fileIds() != null) {
            Set<FileUpload> files = new HashSet<>();
            for (Integer fileId : ticketDTO.fileIds()) {
                FileUpload file = fileUploadRepo.findById(fileId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid file ID: " + fileId));
                files.add(file);
            }
            ticket.setFiles(files);
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

    public List<TicketDTO> getTicketsByStatusId(Integer statusId) {
        return ticketMapper.toDtoList(ticketRepo.findByStatusId(statusId));
    }

    public ResponseDTO deleteTicket(Integer ticketId) {
        ticketRepo.deleteById(ticketId);
        return new ResponseDTO("Ticket deleted successfully");
    }

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

    @Transactional
    public ResponseDTO addResponsibleBaitWorkerToTicket(Integer ticketId, Integer baitWorkerId) {
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

    @Transactional
    public ResponseDTO addLocationToTicket(Integer ticketId, Integer locationId) {
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

    @Transactional
    public ResponseDTO addStatusToTicket(Integer ticketId, Integer statusId) {
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

    @Transactional
    public ResponseDTO addContactToTicket(Integer ticketId, Integer workerId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);

        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        if (workerOpt.isEmpty()) {
            throw new EntityNotFoundException("Worker with id " + workerId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        ClientWorker worker = workerOpt.get();
        ticket.getContacts().add(worker);
        ticketRepo.save(ticket);
        return new ResponseDTO("Contact added to ticket successfully");
    }

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

    public List<TicketDTO> searchAndFilterTickets(String searchTerm, Integer statusId) {
        Specification<Ticket> searchSpec = new TicketSpecification(searchTerm);
        Specification<Ticket> statusSpec = TicketSpecification.hasStatusId(statusId);
        Specification<Ticket> combinedSpec = Specification.where(searchSpec).and(statusSpec);
        return ticketMapper.toDtoList(ticketRepo.findAll(combinedSpec));
    }

    public List<TicketDTO> searchTickets(String searchTerm) {
        Specification<Ticket> spec = new TicketSpecification(searchTerm);
        return ticketMapper.toDtoList(ticketRepo.findAll(spec));
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
