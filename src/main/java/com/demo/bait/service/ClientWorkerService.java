package com.demo.bait.service;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.mapper.ClientWorkerMapper;
import com.demo.bait.repository.ClientWorkerRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ClientWorkerService {

    private ClientWorkerRepo clientWorkerRepo;
    private ClientWorkerMapper clientWorkerMapper;


    public ResponseDTO addWorker(ClientWorkerDTO workerDTO) {
        ClientWorker worker = new ClientWorker();
        worker.setFirstName(workerDTO.firstName());
        worker.setLastName(workerDTO.lastName());
        worker.setEmail(workerDTO.email());
        worker.setPhoneNumber(workerDTO.phoneNumber());
        worker.setTitle(workerDTO.title());
        clientWorkerRepo.save(worker);
        return new ResponseDTO("Client Worker added successfully");
    }

    public List<ClientWorkerDTO> getAllWorkers() {
        return clientWorkerMapper.toDtoList(clientWorkerRepo.findAll());
    }

    public void addEmployer(Integer workerId, Integer clientId) {
        ClientWorker worker = clientWorkerRepo.getReferenceById(workerId);
        worker.setClientId(clientId);
        clientWorkerRepo.save(worker);
    }

    public List<ClientWorkerDTO> getWorkersByClientId(Integer clientId) {
        return clientWorkerMapper.toDtoList(clientWorkerRepo.findByClientId(clientId));
    }
}
