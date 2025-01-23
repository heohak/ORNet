package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.*;
import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.*;
import com.demo.bait.repository.*;
import com.demo.bait.repository.classificator.TicketStatusClassificatorRepo;
import com.demo.bait.service.ClientWorkerServices.ClientWorkerService;
import com.demo.bait.service.DeviceServices.DeviceService;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import com.demo.bait.service.classificator.WorkTypeClassificatorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
    private FileUploadService fileUploadService;
    private WorkTypeClassificatorService workTypeClassificatorService;
    private ClientWorkerService clientWorkerService;
    private DeviceService deviceService;
    private TicketDeviceService ticketDeviceService;


    @Transactional
    public ResponseDTO addTicket(TicketDTO ticketDTO) {
        log.info("Adding new ticket for client ID: {}", ticketDTO.clientId());
        Optional<Client> clientOpt = clientRepo.findById(ticketDTO.clientId());
        if (clientOpt.isEmpty()) {
            log.error("Client with ID: {} not found", ticketDTO.clientId());
            throw new EntityNotFoundException("Client with id " + ticketDTO.clientId() + " not found");
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(ticketDTO.title());
        ticket.setClient(clientOpt.get());
        setTicketBaitNumeration(ticket);
        ticket.setClientNumeration(ticketDTO.clientNumeration());
        ticket.setDescription(ticketDTO.description());
        ticket.setStartDateTime(LocalDateTime.now().withNano(0));

        if (ticketDTO.locationId() != null && locationRepo.findById(ticketDTO.locationId()).isPresent()) {
            ticket.setLocation(locationRepo.getReferenceById(ticketDTO.locationId()));
        }

        log.debug("Setting ticket contacts, work types, and crisis flag");
        if (ticketDTO.contactIds() != null) {
            Set<ClientWorker> contacts = clientWorkerService.contactIdsToClientWorkersSet(ticketDTO.contactIds());
            ticket.setContacts(contacts);
        }

        if (ticketDTO.workTypeIds() != null) {
            Set<WorkTypeClassificator> workTypes = workTypeClassificatorService
                    .workTypeIdsToWorkTypesSet(ticketDTO.workTypeIds());
            ticket.setWorkTypes(workTypes);
        }

        ticket.setCrisis(ticketDTO.crisis() != null ? ticketDTO.crisis() : false);

        if (ticketDTO.statusId() != null && ticketStatusRepo.findById(ticketDTO.statusId()).isPresent()) {
            ticket.setStatus(ticketStatusRepo.getReferenceById(ticketDTO.statusId()));
        }

        if (ticketDTO.baitWorkerId() != null && baitWorkerRepo.findById(ticketDTO.baitWorkerId()).isPresent()) {
            ticket.setBaitWorker(baitWorkerRepo.getReferenceById(ticketDTO.baitWorkerId()));
        }

        log.debug("Setting ticket response, end date, root cause, files, and devices");
        ticket.setResponseDateTime(ticketDTO.responseDateTime());
        ticket.setInsideInfo(ticketDTO.insideInfo());
        ticket.setEndDateTime(ticketDTO.endDateTime());
        ticket.setRootCause(ticketDTO.rootCause());

        if (ticketDTO.fileIds() != null) {
            Set<FileUpload> files = fileUploadService.fileIdsToFilesSet(ticketDTO.fileIds());
            ticket.setFiles(files);
        }

        if (ticketDTO.deviceIds() != null) {
            Set<Device> devices = deviceService.deviceIdsToDevicesSet(ticketDTO.deviceIds());
            ticket.setDevices(devices);
            ticketDeviceService.addCustomerRegisterNos(ticket, devices);
        }

        ticket.setTimeSpent(Duration.ZERO);
        ticket.setPaidTime(Duration.ZERO);
        ticket.setPaid(Boolean.FALSE);
        ticket.setSettled(Boolean.FALSE);

        ticketRepo.save(ticket);
        setTicketUpdateTime(ticket);
        log.info("Ticket successfully added with ID: {}", ticket.getId());
        return new ResponseDTO(ticket.getId().toString());
    }

    @Transactional
    public ResponseDTO deleteTicket(Integer ticketId) {
        log.info("Deleting ticket with ID: {}", ticketId);
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            log.error("Ticket with ID: {} not found", ticketId);
            throw new EntityNotFoundException("Ticket with ID " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        log.debug("Clearing all associations for ticket ID: {}", ticketId);
        ticket.setClient(null);
        ticket.setLocation(null);
        ticket.getContacts().clear();
        ticket.getWorkTypes().clear();
        ticket.getActivities().clear();
        ticket.getFiles().clear();
        ticket.getDevices().clear();

        ticketRepo.delete(ticket);
        log.info("Ticket with ID: {} successfully deleted", ticketId);
        return new ResponseDTO("Ticket deleted successfully");
    }

    @Transactional
    public void addResponsibleBaitWorkerToTicket(Ticket ticket, Integer baitWorkerId) {
        log.info("Adding responsible bait worker ID: {} to ticket ID: {}", baitWorkerId, ticket.getId());
        if (baitWorkerId != null) {
            Optional<BaitWorker> baitWorkerOpt = baitWorkerRepo.findById(baitWorkerId);
            if (baitWorkerOpt.isEmpty()) {
                log.error("Bait worker with ID: {} not found", baitWorkerId);
                throw new EntityNotFoundException("Bait worker with id " + baitWorkerId + " not found");
            }
            ticket.setBaitWorker(baitWorkerOpt.get());
            ticketRepo.save(ticket);
            log.info("Responsible bait worker successfully added to ticket ID: {}", ticket.getId());
        }
    }

    @Transactional
    public void addLocationToTicket(Ticket ticket, Integer locationId) {
        if (locationId != null) {
            Optional<Location> locationOpt = locationRepo.findById(locationId);

            if (locationOpt.isEmpty()) {
                throw new EntityNotFoundException("Location with id " + locationId + " not found");
            }

            Location location = locationOpt.get();
            ticket.setLocation(location);
            ticketRepo.save(ticket);
        }
    }

    @Transactional
    public void addStatusToTicket(Ticket ticket, Integer statusId) {
        if (statusId != null) {
            Optional<TicketStatusClassificator> statusOpt = ticketStatusRepo.findById(statusId);
            if (statusOpt.isEmpty()) {
                throw new EntityNotFoundException("Ticket status with id " + statusId + " not found");
            }
            TicketStatusClassificator status = statusOpt.get();
            ticket.setStatus(status);
            ticketRepo.save(ticket);
        }
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
        return new ResponseDTO("Ticket status added successfully");
    }

    @Transactional
    public void addClientToTicket(Ticket ticket, Integer clientId) {
        if (clientId != null) {
            Optional<Client> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }
            Client client = clientOpt.get();
            ticket.setClient(client);
            ticketRepo.save(ticket);
        }
    }

    @Transactional
    public void updateTicketInsideInfo(Ticket ticket, TicketDTO ticketDTO) {
        if (ticketDTO.insideInfo() != null) {
            ticket.setInsideInfo(ticketDTO.insideInfo());
            ticketRepo.save(ticket);
        }
    }

    @Transactional
    public void updateTicketDescription(Ticket ticket, TicketDTO ticketDTO) {
        if (ticketDTO.description() != null) {
            ticket.setDescription(ticketDTO.description());
            ticketRepo.save(ticket);
        }
    }

    @Transactional
    public void addRootCauseToTicket(Ticket ticket, TicketDTO ticketDTO) {
        if (ticketDTO.rootCause() != null) {
            ticket.setRootCause(ticketDTO.rootCause());
            ticketRepo.save(ticket);
        }
    }

    @Transactional
    public void addResponseDateToTicket(Ticket ticket, TicketDTO ticketDTO) {
        if (ticketDTO.responseDateTime() != null) {
            ticket.setResponseDateTime(ticketDTO.responseDateTime());
            ticketRepo.save(ticket);
        }
    }

    @Transactional
    public void addEndDateToTicket(Ticket ticket, TicketDTO ticketDTO) {
        if (ticketDTO.endDateTime() != null) {
            ticket.setEndDateTime(ticketDTO.endDateTime());
            ticketRepo.save(ticket);
        }
    }

    @Transactional
    public void updateCrisisInTicket(Ticket ticket, TicketDTO ticketDTO) {
        if (ticketDTO.crisis() != null) {
            ticket.setCrisis(ticketDTO.crisis());
            ticketRepo.save(ticket);
        }
    }

    @Transactional
    public void updateTicketClientNumeration(Ticket ticket, TicketDTO ticketDTO) {
        if (ticketDTO.clientNumeration() != null) {
            ticket.setClientNumeration(ticketDTO.clientNumeration());
            ticketRepo.save(ticket);
        }
    }

    @Transactional
    public void setTicketUpdateTime(Ticket ticket) {
        ticket.setUpdateDateTime(LocalDateTime.now().withNano(0));
        ticketRepo.save(ticket);
    }

    @Transactional
    public ResponseDTO updateWholeTicket(Integer ticketId, TicketDTO ticketDTO) {
        log.info("Updating whole ticket with ID: {}", ticketId);
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            log.error("Ticket with ID: {} not found", ticketId);
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        addResponseDateToTicket(ticket, ticketDTO);
        addEndDateToTicket(ticket, ticketDTO);
        updateTicketClientNumeration(ticket, ticketDTO);
        updateCrisisInTicket(ticket, ticketDTO);
        ticketWorkTypeService.addWorkTypeToTicket(ticket, ticketDTO);
        addResponsibleBaitWorkerToTicket(ticket, ticketDTO.baitWorkerId());
        addClientToTicket(ticket, ticketDTO.clientId());
        addLocationToTicket(ticket, ticketDTO.locationId());
        addStatusToTicket(ticket, ticketDTO.statusId());
        addRootCauseToTicket(ticket, ticketDTO);
        updateTicketDescription(ticket, ticketDTO);
        updateTicketInsideInfo(ticket, ticketDTO);
        ticketContactsService.addContactsToTicket(ticket, ticketDTO);
        ticketDeviceService.addDevicesToTicket(ticket, ticketDTO);
        setTicketUpdateTime(ticket);

        log.info("Whole ticket updated successfully with ID: {}", ticketId);
        return new ResponseDTO("Whole ticket updated successfully");
    }

    @Transactional
    public void setTicketBaitNumeration(Ticket ticket) {
        ticketRepo.save(ticket);
        LocalDate now = LocalDate.now();
        String yy = String.valueOf(now.getYear()).substring(2);
        String nn;
        if (ticket.getId() < 10) {
            nn = String.format("00%d", ticket.getId());
        } else if (ticket.getId() < 100) {
            nn = String.format("0%d", ticket.getId());
        } else {
            nn = String.valueOf(ticket.getId());
        }

        String numeration = String.format("T%s-%s", yy, nn);
        ticket.setBaitNumeration(numeration);
    }

    @Transactional
    public void addTimeSpent(Ticket ticket, Integer hours, Integer minutes, Boolean paid) {
        Duration timeSpent = addDuration(ticket.getTimeSpent(), hours, minutes);
        ticket.setTimeSpent(timeSpent);

        if (Boolean.TRUE.equals(paid)) {
            Duration paidTime = addDuration(ticket.getPaidTime(), hours, minutes);
            ticket.setPaidTime(paidTime);
        }

        ticketRepo.save(ticket);
    }

    private Duration addDuration(Duration duration, Integer hours, Integer minutes) {
        if (duration == null) {
            duration = Duration.ZERO;
        }
        if (hours != null) {
            duration = duration.plusHours(hours);
        }
        if (minutes != null) {
            duration = duration.plusMinutes(minutes);
        }
        return duration;
    }

    public TicketDTO getTicketById(Integer ticketId) {
        log.info("Retrieving ticket with ID: {}", ticketId);
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            log.error("Ticket with ID: {} not found", ticketId);
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        log.info("Successfully retrieved ticket with ID: {}", ticketId);
        return ticketMapper.toDto(ticketOpt.get());
    }

    public List<TicketDTO> getTicketsByClientId(Integer clientId) {
        log.info("Retrieving tickets for client ID: {}", clientId);
        List<TicketDTO> tickets = ticketMapper.toDtoList(ticketRepo.findByClientId(clientId));
        log.info("Retrieved {} tickets for client ID: {}", tickets.size(), clientId);
        return tickets;
    }

    public List<TicketDTO> getAllTickets() {
        log.info("Retrieving all tickets");
        List<TicketDTO> tickets = ticketMapper.toDtoList(ticketRepo.findAll());
        log.info("Successfully retrieved {} tickets", tickets.size());
        return tickets;
    }
}
