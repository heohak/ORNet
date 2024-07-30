package com.demo.bait.service;

import com.demo.bait.dto.*;
import com.demo.bait.dto.classificator.WorkTypeClassificatorDTO;
import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.*;
import com.demo.bait.mapper.classificator.WorkTypeClassificatorMapper;
import com.demo.bait.repository.*;
import com.demo.bait.repository.classificator.TicketStatusClassificatorRepo;
import com.demo.bait.repository.classificator.WorkTypeClassificatorRepo;
import com.demo.bait.specification.TicketSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private ClientWorkerRepo clientWorkerRepo;
    private ClientWorkerMapper clientWorkerMapper;
    private TicketStatusClassificatorRepo ticketStatusRepo;
    private BaitWorkerRepo baitWorkerRepo;
    private MaintenanceRepo maintenanceRepo;
    private MaintenanceMapper maintenanceMapper;
    private FileUploadRepo fileUploadRepo;
    private FileUploadService fileUploadService;
    private FileUploadMapper fileUploadMapper;
    private CommentRepo commentRepo;
    private CommentMapper commentMapper;
    private CommentService commentService;
    private WorkTypeClassificatorRepo workTypeClassificatorRepo;
    private WorkTypeClassificatorMapper workTypeClassificatorMapper;
    private PaidWorkService paidWorkService;
    private PaidWorkMapper paidWorkMapper;
    private PaidWorkRepo paidWorkRepo;


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
            Set<ClientWorker> contacts = new HashSet<>();
            for (Integer id : ticketDTO.contactIds()) {
                ClientWorker contact = clientWorkerRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid worker ID: " + id));
                contacts.add(contact);
            }
            ticket.setContacts(contacts);
        }

        if (ticketDTO.workTypeIds() != null) {
            Set<WorkTypeClassificator> workTypes = new HashSet<>();
            for (Integer id : ticketDTO.workTypeIds()) {
                WorkTypeClassificator workType = workTypeClassificatorRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid work type classificator ID: " + id));
                workTypes.add(workType);
            }
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
            Set<Comment> comments = new HashSet<>();
            for (Integer commentId : ticketDTO.commentIds()) {
                Comment comment = commentRepo.findById(commentId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));
                comments.add(comment);
            }
            ticket.setComments(comments);
        }

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
        setTicketUpdateTime(ticket.getId());
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
//        return ticketMapper.toDtoList(ticketRepo.findByStatusId(statusId));

        Specification<Ticket> spec = TicketSpecification.hasStatusId(statusId);
        return ticketMapper.toDtoList(ticketRepo.findAll(spec));
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
    public ResponseDTO addClientToTicket(Integer ticketId, Integer clientId) {
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
            return new ResponseDTO("Client added to ticket");
        }
        return new ResponseDTO("No client added");
    }

    @Transactional
    public ResponseDTO addCommentToTicket(Integer ticketId, String newComment) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        Comment comment = commentService.addComment(newComment);
        ticket.getComments().add(comment);
        ticketRepo.save(ticket);
        return new ResponseDTO("Comment added successfully");
    }

    @Transactional
    public ResponseDTO addWorkTypeToTicket(Integer ticketId, TicketDTO ticketDTO) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        if (ticketDTO.workTypeIds() != null) {
            Set<WorkTypeClassificator> workTypes = new HashSet<>();
            for (Integer id : ticketDTO.workTypeIds()) {
                WorkTypeClassificator workType = workTypeClassificatorRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid work type classificator ID: " + id));
                workTypes.add(workType);
            }
            ticket.setWorkTypes(workTypes);
        }
        ticketRepo.save(ticket);
        return new ResponseDTO("Work type classificators added to ticket");
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
    public ResponseDTO updateTicketDescription(Integer ticketId, TicketDTO ticketDTO) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        ticket.setDescription(ticketDTO.description());
        ticketRepo.save(ticket);
        return new ResponseDTO("Ticket description updated successfully");
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
        ticket.setUpdateDateTime(LocalDateTime.now());
        ticketRepo.save(ticket);
    }

    @Transactional
    public ResponseDTO updateWholeTicket(Integer ticketId, TicketDTO ticketDTO) {
        addResponseDateToTicket(ticketId, ticketDTO);
        // endDateTime??
        // updateDateTime??
        updateCrisisInTicket(ticketId, ticketDTO);
        updateRemoteInTicket(ticketId, ticketDTO);
        addWorkTypeToTicket(ticketId, ticketDTO);
        addResponsibleBaitWorkerToTicket(ticketId, ticketDTO.baitWorkerId());
        addClientToTicket(ticketId, ticketDTO.clientId());
        addLocationToTicket(ticketId, ticketDTO.locationId());
        addStatusToTicket(ticketId, ticketDTO.statusId());
        addRootCauseToTicket(ticketId, ticketDTO);
        updateTicketDescription(ticketId, ticketDTO);
        updateTicketResponseAndInsideInfo(ticketId, ticketDTO);
        if (ticketDTO.contactIds() != null) {
            for (Integer contactId : ticketDTO.contactIds()) {
                addContactToTicket(ticketId, contactId);
            }
        }
        setTicketUpdateTime(ticketId);
        return new ResponseDTO("Whole ticket updated successfully");
    }

//    public List<TicketDTO> searchAndFilterTickets(String searchTerm, Integer statusId) {
//        Specification<Ticket> searchSpec = new TicketSpecification(searchTerm);
//        Specification<Ticket> statusSpec = TicketSpecification.hasStatusId(statusId);
//        Specification<Ticket> combinedSpec = Specification.where(searchSpec).and(statusSpec);
//        return ticketMapper.toDtoList(ticketRepo.findAll(combinedSpec));
//    }
//
//    public List<TicketDTO> searchTickets(String searchTerm) {
//        Specification<Ticket> spec = new TicketSpecification(searchTerm);
//        return ticketMapper.toDtoList(ticketRepo.findAll(spec));
//    }
//
//    public List<TicketDTO> searchAndFilterCrisisTickets(String searchTerm, Integer statusId, Boolean crisis) {
//        Specification<Ticket> searchSpec = new TicketSpecification(searchTerm);
//        Specification<Ticket> statusSpec = TicketSpecification.hasStatusId(statusId);
//        Specification<Ticket> crisisSpec = TicketSpecification.isCrisis(crisis);
//        Specification<Ticket> combinedSpec = Specification.where(searchSpec).and(statusSpec).and(crisisSpec);
//        return ticketMapper.toDtoList(ticketRepo.findAll(combinedSpec));
//    }

    public List<TicketDTO> searchAndFilterTickets(String searchTerm, Integer statusId, Boolean crisis, Boolean paid,
                                                  Integer workTypeId) {
        Specification<Ticket> combinedSpec = Specification.where(null);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Specification<Ticket> searchSpec = new TicketSpecification(searchTerm);
            combinedSpec = combinedSpec.and(searchSpec);
        }

        if (statusId != null) {
            Specification<Ticket> statusSpec = TicketSpecification.hasStatusId(statusId);
            combinedSpec = combinedSpec.and(statusSpec);
        }

        if (crisis != null) {
            Specification<Ticket> crisisSpec = TicketSpecification.isCrisis(crisis);
            combinedSpec = combinedSpec.and(crisisSpec);
        }

        if (paid != null) {
            Specification<Ticket> paidSpec = TicketSpecification.isPaidWork(paid);
            combinedSpec = combinedSpec.and(paidSpec);
        }

        if (workTypeId != null) {
            Specification<Ticket> workTypeSpec = TicketSpecification.hasWorkTypeId(workTypeId);
            combinedSpec = combinedSpec.and(workTypeSpec);
        }

        return ticketMapper.toDtoList(ticketRepo.findAll(combinedSpec));
    }

    public List<MaintenanceDTO> getTicketMaintenances(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);

        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }

        Ticket ticket = ticketOpt.get();
        return maintenanceMapper.toDtoList(ticket.getMaintenances().stream().toList());
    }

    public List<CommentDTO> getTicketComments(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        return commentMapper.toDtoList(ticket.getComments().stream().toList());
    }

    public List<FileUploadDTO> getTicketFiles(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        return fileUploadMapper.toDtoList(ticket.getFiles().stream().toList());
    }

    public List<ClientWorkerDTO> getTicketContacts(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        return clientWorkerMapper.toDtoList(ticket.getContacts().stream().toList());
    }

    public List<WorkTypeClassificatorDTO> getTicketWorkTypes(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        return workTypeClassificatorMapper.toDtoList(ticket.getWorkTypes().stream().toList());
    }

    @Transactional
    public ResponseDTO changeTicketToPaidTicket(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        if (ticket.getPaidWork() == null) {
            PaidWork paidWork = paidWorkService.createPaidWork();
            ticket.setPaidWork(paidWork);
            ticketRepo.save(ticket);
            return new ResponseDTO("Ticket changed to paid ticket successfully");
        }
        return new ResponseDTO("Ticket is already a paid ticket");
    }

    @Transactional
    public ResponseDTO addTimeToTicketPaidWork(Integer ticketId, Integer hours, Integer minutes) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        PaidWork paidWork = ticket.getPaidWork();
        if (paidWork != null) {
            paidWorkService.addTimeToPaidWork(paidWork.getId(), hours, minutes);
            return new ResponseDTO("Time added to paid ticket successfully");
        }
        return new ResponseDTO("Ticket is not a paid ticket");
    }

    public PaidWorkDTO getTicketPaidWork(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        PaidWork paidWork = ticket.getPaidWork();
        if (paidWork == null) {
            throw new IllegalArgumentException("Ticket does not have paid work");
        }
        return paidWorkMapper.toDto(paidWork);
    }

    @Transactional
    public ResponseDTO settleTicketPaidWork(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        PaidWork paidWork = ticket.getPaidWork();
        return paidWorkService.setPaidWorkSettled(paidWork.getId());
    }
}
