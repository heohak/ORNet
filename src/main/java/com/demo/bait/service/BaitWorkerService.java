package com.demo.bait.service;

import com.demo.bait.dto.BaitWorkerDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.BaitWorker;
import com.demo.bait.mapper.BaitWorkerMapper;
import com.demo.bait.repository.BaitWorkerRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BaitWorkerService {

    private BaitWorkerRepo baitWorkerRepo;
    private BaitWorkerMapper baitWorkerMapper;

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

    public List<BaitWorkerDTO> getAllWorkers() {
        return baitWorkerMapper.toDtoList(baitWorkerRepo.findAll());
    }
}
