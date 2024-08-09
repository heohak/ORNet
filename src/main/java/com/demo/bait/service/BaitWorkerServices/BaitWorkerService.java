package com.demo.bait.service.BaitWorkerServices;

import com.demo.bait.dto.BaitWorkerDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.BaitWorker;
import com.demo.bait.mapper.BaitWorkerMapper;
import com.demo.bait.repository.BaitWorkerRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class BaitWorkerService {

    private BaitWorkerRepo baitWorkerRepo;
    private BaitWorkerMapper baitWorkerMapper;

    @Transactional
    public ResponseDTO addWorker(BaitWorkerDTO workerDTO) {
        BaitWorker worker = new BaitWorker();
        worker.setFirstName(workerDTO.firstName());
        worker.setLastName(workerDTO.lastName());
        worker.setEmail(workerDTO.email());
        worker.setPhoneNumber(workerDTO.phoneNumber());
        worker.setTitle(workerDTO.title());
        baitWorkerRepo.save(worker);
        return new ResponseDTO("Bait Worker added successfully");
    }

    @Transactional
    public ResponseDTO deleteBaitWorker(Integer baitWorkerId) {
        baitWorkerRepo.deleteById(baitWorkerId);
        return new ResponseDTO("Bait Worker deleted successfully");
    }

    @Transactional
    public ResponseDTO updateBaitWorker(Integer baitWorkerId, BaitWorkerDTO baitWorkerDTO) {
        Optional<BaitWorker> baitWorkerOpt = baitWorkerRepo.findById(baitWorkerId);
        if (baitWorkerOpt.isEmpty()) {
            throw new EntityNotFoundException("Bait worker with id " + baitWorkerId + " not found");
        }
        BaitWorker worker = baitWorkerOpt.get();
        updateFirstName(worker, baitWorkerDTO);
        updateLastName(worker, baitWorkerDTO);
        updateEmail(worker, baitWorkerDTO);
        updatePhoneNumber(worker, baitWorkerDTO);
        updateTitle(worker, baitWorkerDTO);
        baitWorkerRepo.save(worker);
        return new ResponseDTO("Bait Worker updated successfully");
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
        return baitWorkerMapper.toDtoList(baitWorkerRepo.findAll());
    }

    public BaitWorkerDTO getBaitWorkerById(Integer workerId) {
        Optional<BaitWorker> baitWorkerOpt = baitWorkerRepo.findById(workerId);
        if (baitWorkerOpt.isEmpty()) {
            throw new EntityNotFoundException("Bait worker with id " + workerId + " not found");
        }
        return baitWorkerMapper.toDto(baitWorkerOpt.get());
    }
}
