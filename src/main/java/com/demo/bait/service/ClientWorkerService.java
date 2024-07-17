package com.demo.bait.service;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.ClientWorkerRoleClassificatorDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import com.demo.bait.mapper.ClientWorkerMapper;
import com.demo.bait.mapper.LocationMapper;
import com.demo.bait.mapper.classificator.ClientWorkerRoleClassificatorMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.ClientWorkerRepo;
import com.demo.bait.repository.LocationRepo;
import com.demo.bait.repository.classificator.ClientWorkerRoleClassificatorRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
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
public class ClientWorkerService {

    private ClientWorkerRepo clientWorkerRepo;
    private ClientWorkerMapper clientWorkerMapper;
    private ClientRepo clientRepo;
    private LocationRepo locationRepo;
    private LocationMapper locationMapper;
    private ClientWorkerRoleClassificatorRepo workerRoleClassificatorRepo;
    private ClientWorkerRoleClassificatorMapper workerRoleClassificatorMapper;


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
        if (workerDTO.locationId() != null && locationRepo.findById(workerDTO.locationId()).isPresent()) {
            worker.setLocation(locationRepo.getReferenceById(workerDTO.locationId()));
        }

        if(workerDTO.roleIds() != null) {
            Set<ClientWorkerRoleClassificator> roles = new HashSet<>();
            for (Integer roleId : workerDTO.roleIds()) {
                ClientWorkerRoleClassificator role = workerRoleClassificatorRepo.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid client worker role ID " + roleId));
                roles.add(role);
            }
            worker.setRoles(roles);
        }

        clientWorkerRepo.save(worker);
        return new ResponseDTO("Client Worker added successfully");
    }

    public List<ClientWorkerDTO> getAllWorkers() {
        return clientWorkerMapper.toDtoList(clientWorkerRepo.findAll());
    }

    @Transactional
    public ResponseDTO addEmployer(Integer workerId, Integer clientId) {
//        ClientWorker worker = clientWorkerRepo.getReferenceById(workerId);
//        worker.setClientId(clientId);
//        clientWorkerRepo.save(worker);
        Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
        Optional<Client> clientOpt = clientRepo.findById(clientId);

        if (workerOpt.isEmpty()) {
            throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
        }
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }

        ClientWorker worker = workerOpt.get();
        Client client = clientOpt.get();
        worker.setClient(client);
        clientWorkerRepo.save(worker);
        return new ResponseDTO("Employer added successfully");
    }

    @Transactional
    public ResponseDTO addLocationToEmployee(Integer workerId, Integer locationId) {
        Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
        Optional<Location> locationOpt = locationRepo.findById(locationId);

        if (workerOpt.isEmpty()) {
            throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
        }
        if (locationOpt.isEmpty()) {
            throw new EntityNotFoundException("Location with id " + locationId + " not found");
        }

        ClientWorker worker = workerOpt.get();
        Location location = locationOpt.get();
        worker.setLocation(location);
        clientWorkerRepo.save(worker);
        return new ResponseDTO("Location to worker added successfully");
    }

    @Transactional
    public ResponseDTO addRoleToEmployee(Integer workerId, Integer roleId) {
        Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
        Optional<ClientWorkerRoleClassificator> roleOpt = workerRoleClassificatorRepo.findById(roleId);

        if (workerOpt.isEmpty()) {
            throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
        }
        if (roleOpt.isEmpty()) {
            throw new EntityNotFoundException("Client worker role classificator with id " + roleId + " not found");
        }

        ClientWorker worker = workerOpt.get();
        ClientWorkerRoleClassificator role = roleOpt.get();
        worker.getRoles().add(role);
        clientWorkerRepo.save(worker);
        return new ResponseDTO("Client worker role added successfully to worker");
    }

    public List<ClientWorkerDTO> getWorkersByClientId(Integer clientId) {
        return clientWorkerMapper.toDtoList(clientWorkerRepo.findByClientId(clientId));
    }

    public ResponseDTO deleteWorker(Integer workerId) {
        clientWorkerRepo.deleteById(workerId);
        return new ResponseDTO("Worker deleted successfully");
    }

    public LocationDTO getWorkerLocation(Integer workerId) {
        Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
        if (workerOpt.isEmpty()) {
            throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
        }
        ClientWorker worker = workerOpt.get();
        Location location = worker.getLocation();
        return locationMapper.toDto(location);
    }

    public List<ClientWorkerRoleClassificatorDTO> getWorkerRole(Integer workerId) {
        Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
        if (workerOpt.isEmpty()) {
            throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
        }
        ClientWorker worker = workerOpt.get();
        return workerRoleClassificatorMapper.toDtoList(worker.getRoles().stream().toList());
    }

    public List<ClientWorkerDTO> getWorkersByRoleId(Integer roleId) {
        return clientWorkerMapper.toDtoList(clientWorkerRepo.findByRoleId(roleId));
    }
}
