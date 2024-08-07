package com.demo.bait.service.ClientWorkerServices;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.ClientWorkerRoleClassificatorDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import com.demo.bait.mapper.ClientWorkerMapper;
import com.demo.bait.mapper.LocationMapper;
import com.demo.bait.mapper.classificator.ClientWorkerRoleClassificatorMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.ClientWorkerRepo;
import com.demo.bait.repository.LocationRepo;
import com.demo.bait.repository.classificator.ClientWorkerRoleClassificatorRepo;
import com.demo.bait.service.classificator.ClientWorkerRoleClassificatorService;
import com.demo.bait.specification.ClientSpecification;
import com.demo.bait.specification.ClientWorkerSpecification;
import com.demo.bait.specification.DeviceSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
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
    private ClientWorkerRoleClassificatorService clientWorkerRoleClassificatorService;

    @Transactional
    public ResponseDTO addWorker(ClientWorkerDTO workerDTO) {
        ClientWorker worker = new ClientWorker();
        worker.setFirstName(workerDTO.firstName());
        worker.setLastName(workerDTO.lastName());
        worker.setEmail(workerDTO.email());
        worker.setPhoneNumber(workerDTO.phoneNumber());
        worker.setTitle(workerDTO.title());

        updateClient(worker, workerDTO);
        updateLocation(worker, workerDTO);
        updateRoles(worker, workerDTO);

        clientWorkerRepo.save(worker);
        return new ResponseDTO(worker.getId().toString());
    }

    @Transactional
    public ResponseDTO addEmployer(Integer workerId, Integer clientId) {
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
    public ResponseDTO deleteWorker(Integer workerId) {
        clientWorkerRepo.deleteById(workerId);
        return new ResponseDTO("Worker deleted successfully");
    }

    @Transactional
    public ResponseDTO updateClientWorker(Integer workerId, ClientWorkerDTO clientWorkerDTO) {
        Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
        if (workerOpt.isEmpty()) {
            throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
        }
        ClientWorker worker = workerOpt.get();

        updateFirstName(worker, clientWorkerDTO);
        updateLastName(worker, clientWorkerDTO);
        updateEmail(worker, clientWorkerDTO);
        updatePhoneNumber(worker, clientWorkerDTO);
        updateTitle(worker, clientWorkerDTO);
        updateClient(worker, clientWorkerDTO);
        updateLocation(worker, clientWorkerDTO);
        updateRoles(worker, clientWorkerDTO);
        clientWorkerRepo.save(worker);
        return new ResponseDTO("Client worker updated successfully");
    }

    public void updateFirstName(ClientWorker worker, ClientWorkerDTO clientWorkerDTO) {
        if (clientWorkerDTO.firstName() != null) {
            worker.setFirstName(clientWorkerDTO.firstName());
        }
    }

    public void updateLastName(ClientWorker worker, ClientWorkerDTO clientWorkerDTO) {
        if (clientWorkerDTO.lastName() != null) {
            worker.setLastName(clientWorkerDTO.lastName());
        }
    }

    public void updateEmail(ClientWorker worker, ClientWorkerDTO clientWorkerDTO) {
        if (clientWorkerDTO.email() != null) {
            worker.setEmail(clientWorkerDTO.email());
        }
    }

    public void updatePhoneNumber(ClientWorker worker, ClientWorkerDTO clientWorkerDTO) {
        if (clientWorkerDTO.phoneNumber() != null) {
            worker.setPhoneNumber(clientWorkerDTO.phoneNumber());
        }
    }

    public void updateTitle(ClientWorker worker, ClientWorkerDTO clientWorkerDTO) {
        if (clientWorkerDTO.title() != null) {
            worker.setTitle(clientWorkerDTO.title());
        }
    }

    public void updateClient(ClientWorker worker, ClientWorkerDTO clientWorkerDTO) {
        if (clientWorkerDTO.clientId() != null) {
            Optional<Client> clientOpt = clientRepo.findById(clientWorkerDTO.clientId());
            clientOpt.ifPresent(worker::setClient);
        }
    }

    public void updateLocation(ClientWorker worker, ClientWorkerDTO clientWorkerDTO) {
        if (clientWorkerDTO.locationId() != null) {
            Optional<Location> locationOpt = locationRepo.findById(clientWorkerDTO.locationId());
            locationOpt.ifPresent(worker::setLocation);
        }
    }

    public void updateRoles(ClientWorker worker, ClientWorkerDTO clientWorkerDTO) {
        if (clientWorkerDTO.roleIds() != null) {
            Set<ClientWorkerRoleClassificator> roles = clientWorkerRoleClassificatorService
                    .roleIdsToRolesSet(clientWorkerDTO.roleIds());
            worker.setRoles(roles);
        }
    }

    public List<ClientWorkerDTO> getAllWorkers() {
        return clientWorkerMapper.toDtoList(clientWorkerRepo.findAll());
    }

    public List<ClientWorkerDTO> getWorkersByClientId(Integer clientId) {
        return clientWorkerMapper.toDtoList(clientWorkerRepo.findByClientId(clientId));
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

    public Set<ClientWorker> contactIdsToClientWorkersSet(List<Integer> contactIds) {
        Set<ClientWorker> contacts = new HashSet<>();
        for (Integer id : contactIds) {
            ClientWorker contact = clientWorkerRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid worker ID: " + id));
            contacts.add(contact);
        }
        return contacts;
    }
}
