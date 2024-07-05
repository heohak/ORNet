package com.demo.bait.service;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.mapper.ClientWorkerMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.ClientWorkerRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ClientWorkerService {

    private ClientWorkerRepo clientWorkerRepo;
    private ClientWorkerMapper clientWorkerMapper;
    private ClientRepo clientRepo;


    public ResponseDTO addWorker(ClientWorkerDTO workerDTO) {
        ClientWorker worker = new ClientWorker();
        worker.setFirstName(workerDTO.firstName());
        worker.setLastName(workerDTO.lastName());
        worker.setEmail(workerDTO.email());
        worker.setPhoneNumber(workerDTO.phoneNumber());
        worker.setTitle(workerDTO.title());
        if (workerDTO.clientId() != null && clientRepo.findById(workerDTO.clientId()).isPresent()) {
            worker.setClient(clientRepo.getReferenceById(workerDTO.clientId()));
        }
        clientWorkerRepo.save(worker);
        return new ResponseDTO("Client Worker added successfully");
    }

    public List<ClientWorkerDTO> getAllWorkers() {
        return clientWorkerMapper.toDtoList(clientWorkerRepo.findAll());
    }

    @Transactional
    public void addEmployer(Integer workerId, Integer clientId) {
//        ClientWorker worker = clientWorkerRepo.getReferenceById(workerId);
//        worker.setClientId(clientId);
//        clientWorkerRepo.save(worker);
        Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
        Optional<Client> clientOpt = clientRepo.findById(clientId);

        if (workerOpt.isPresent() && clientOpt.isPresent()) {
            ClientWorker worker = workerOpt.get();
            Client client = clientOpt.get();
            worker.setClient(client);
            clientWorkerRepo.save(worker);
        } else {
            if (workerOpt.isEmpty()) {
                throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
            }
            if (clientOpt.isEmpty()) {
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }
        }
    }

    public List<ClientWorkerDTO> getWorkersByClientId(Integer clientId) {
        return clientWorkerMapper.toDtoList(clientWorkerRepo.findByClientId(clientId));
    }

    public ResponseDTO deleteWorker(Integer workerId) {
        clientWorkerRepo.deleteById(workerId);
        return new ResponseDTO("Worker deleted successfully");
    }
}
