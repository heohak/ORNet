package com.demo.bait.service.BaitWorkerServices;

import com.demo.bait.dto.BaitWorkerDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.BaitWorker;
import com.demo.bait.entity.ClientActivity;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.BaitWorkerMapper;
import com.demo.bait.repository.BaitWorkerRepo;
import com.demo.bait.repository.ClientActivityRepo;
import com.demo.bait.repository.TicketRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class BaitWorkerService {

    private BaitWorkerRepo baitWorkerRepo;
    private BaitWorkerMapper baitWorkerMapper;
    private TicketRepo ticketRepo;
    private ClientActivityRepo clientActivityRepo;

    @Transactional
    public ResponseDTO addWorker(BaitWorkerDTO workerDTO) {
        log.info("Adding new Bait Worker: {}", workerDTO);
        try {
            BaitWorker worker = new BaitWorker();
            worker.setFirstName(workerDTO.firstName());
            worker.setLastName(workerDTO.lastName());
            worker.setEmail(workerDTO.email());
            worker.setPhoneNumber(workerDTO.phoneNumber());
            worker.setTitle(workerDTO.title());
            baitWorkerRepo.save(worker);
            log.info("Bait Worker successfully added: {}", worker);
            return new ResponseDTO("Bait Worker added successfully");
        } catch (Exception e) {
            log.error("Error occurred while adding Bait Worker: {}", workerDTO, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO deleteBaitWorker(Integer baitWorkerId) {
        log.info("Deleting Bait Worker with ID: {}", baitWorkerId);
        try {
            Optional<BaitWorker> baitWorkerOpt = baitWorkerRepo.findById(baitWorkerId);
            if (baitWorkerOpt.isEmpty()) {
                log.warn("Bait Worker with ID {} not found", baitWorkerId);
                throw new EntityNotFoundException("Bait Worker with id " + baitWorkerId + " not found");
            }
            BaitWorker baitWorker = baitWorkerOpt.get();

            log.debug("Unlinking tickets for Bait Worker ID: {}", baitWorkerId);
            List<Ticket> tickets = ticketRepo.findByBaitWorker(baitWorker);
            for (Ticket ticket : tickets) {
                ticket.setBaitWorker(null);
                ticketRepo.save(ticket);
            }

            log.debug("Unlinking client activities for Bait Worker ID: {}", baitWorkerId);
            List<ClientActivity> clientActivities = clientActivityRepo.findByBaitWorker(baitWorker);
            for (ClientActivity clientActivity : clientActivities) {
                clientActivity.setBaitWorker(null);
                clientActivityRepo.save(clientActivity);
            }

            baitWorkerRepo.delete(baitWorker);
            log.info("Bait Worker with ID {} successfully deleted", baitWorkerId);
            return new ResponseDTO("Bait Worker deleted successfully");
        } catch (Exception e) {
            log.error("Error occurred while deleting Bait Worker with ID: {}", baitWorkerId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO updateBaitWorker(Integer baitWorkerId, BaitWorkerDTO baitWorkerDTO) {
        log.info("Updating Bait Worker with ID: {}", baitWorkerId);
        try {
            Optional<BaitWorker> baitWorkerOpt = baitWorkerRepo.findById(baitWorkerId);
            if (baitWorkerOpt.isEmpty()) {
                log.warn("Bait Worker with ID {} not found", baitWorkerId);
                throw new EntityNotFoundException("Bait Worker with id " + baitWorkerId + " not found");
            }
            BaitWorker worker = baitWorkerOpt.get();
            updateFirstName(worker, baitWorkerDTO);
            updateLastName(worker, baitWorkerDTO);
            updateEmail(worker, baitWorkerDTO);
            updatePhoneNumber(worker, baitWorkerDTO);
            updateTitle(worker, baitWorkerDTO);
            baitWorkerRepo.save(worker);
            log.info("Bait Worker with ID {} successfully updated", baitWorkerId);
            return new ResponseDTO("Bait Worker updated successfully");
        } catch (Exception e) {
            log.error("Error occurred while updating Bait Worker with ID: {}", baitWorkerId, e);
            throw e;
        }
    }

    public void updateFirstName(BaitWorker worker, BaitWorkerDTO baitWorkerDTO) {
        if (baitWorkerDTO.firstName() != null) {
            worker.setFirstName(baitWorkerDTO.firstName());
        }
    }

    public void updateLastName(BaitWorker worker, BaitWorkerDTO baitWorkerDTO) {
        if (baitWorkerDTO.lastName() != null) {
            worker.setLastName(baitWorkerDTO.lastName());
        }
    }

    public void updateEmail(BaitWorker worker, BaitWorkerDTO baitWorkerDTO) {
        if (baitWorkerDTO.email() != null) {
            worker.setEmail(baitWorkerDTO.email());
        }
    }

    public void updatePhoneNumber(BaitWorker worker, BaitWorkerDTO baitWorkerDTO) {
        if (baitWorkerDTO.phoneNumber() != null) {
            worker.setPhoneNumber(baitWorkerDTO.phoneNumber());
        }
    }

    public void updateTitle(BaitWorker worker, BaitWorkerDTO baitWorkerDTO) {
        if (baitWorkerDTO.title() != null) {
            worker.setTitle(baitWorkerDTO.title());
        }
    }

    public List<BaitWorkerDTO> getAllWorkers() {
        log.info("Fetching all Bait Workers");
        List<BaitWorkerDTO> workers = baitWorkerMapper.toDtoList(baitWorkerRepo.findAll());
        log.debug("Fetched Bait Workers: {}", workers);
        return workers;
    }

    public BaitWorkerDTO getBaitWorkerById(Integer workerId) {
        log.info("Fetching Bait Worker with ID: {}", workerId);
        Optional<BaitWorker> baitWorkerOpt = baitWorkerRepo.findById(workerId);
        if (baitWorkerOpt.isEmpty()) {
            log.warn("Bait Worker with ID {} not found", workerId);
            throw new EntityNotFoundException("Bait Worker with id " + workerId + " not found");
        }
        BaitWorkerDTO workerDTO = baitWorkerMapper.toDto(baitWorkerOpt.get());
        log.debug("Fetched Bait Worker: {}", workerDTO);
        return workerDTO;
    }

    public Set<BaitWorker> baitWorkerIdsToBaitWorkersSet(List<Integer> baitWorkerIds) {
        log.info("Fetching bait workers by IDs: {}", baitWorkerIds);
        Set<BaitWorker> baitWorkers = new HashSet<>();
        for (Integer baitWorkerId : baitWorkerIds) {
            BaitWorker baitWorker = baitWorkerRepo.findById(baitWorkerId)
                    .orElseThrow(() -> {
                        log.error("Invalid bait worker ID: {}", baitWorkerId);
                        return new IllegalArgumentException("Invalid bait worker ID: " + baitWorkerId);
                    });
            baitWorkers.add(baitWorker);
        }
        log.info("Fetched bait workers: {}", baitWorkers.size());
        return baitWorkers;
    }
}
