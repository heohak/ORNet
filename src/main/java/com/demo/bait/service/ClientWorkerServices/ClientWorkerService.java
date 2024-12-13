package com.demo.bait.service.ClientWorkerServices;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.ClientWorkerRoleClassificatorDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import com.demo.bait.mapper.ClientMapper;
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
    private ClientMapper clientMapper;

    @Transactional
    public ResponseDTO addWorker(ClientWorkerDTO workerDTO) {
        log.info("Adding a new Client Worker: {}", workerDTO);
        try {
            ClientWorker worker = new ClientWorker();
            worker.setFirstName(workerDTO.firstName());
            worker.setLastName(workerDTO.lastName());
            worker.setEmail(workerDTO.email());
            worker.setPhoneNumber(workerDTO.phoneNumber());
            worker.setTitle(workerDTO.title());

            updateClient(worker, workerDTO);
            updateLocation(worker, workerDTO);
            updateRoles(worker, workerDTO);

            worker.setFavorite(workerDTO.favorite() != null ? workerDTO.favorite() : false);
            worker.setComment(workerDTO.comment());

            clientWorkerRepo.save(worker);
            log.info("Successfully added Client Worker with ID: {}", worker.getId());
            return new ResponseDTO(worker.getId().toString());
        } catch (Exception e) {
            log.error("Error while adding Client Worker: {}", workerDTO, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO addEmployer(Integer workerId, Integer clientId) {
        log.info("Adding employer with ID: {} to worker with ID: {}", clientId, workerId);
        try {
            Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
            Optional<Client> clientOpt = clientRepo.findById(clientId);

            if (workerOpt.isEmpty()) {
                log.warn("Worker with ID {} not found", workerId);
                throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
            }
            if (clientOpt.isEmpty()) {
                log.warn("Client with ID {} not found", clientId);
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }

            ClientWorker worker = workerOpt.get();
            Client client = clientOpt.get();
            worker.setClient(client);
            clientWorkerRepo.save(worker);

            log.info("Successfully added employer with ID: {} to worker with ID: {}", clientId, workerId);
            return new ResponseDTO("Employer added successfully");
        } catch (Exception e) {
            log.error("Error while adding employer with ID: {} to worker with ID: {}", clientId, workerId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO addLocationToEmployee(Integer workerId, Integer locationId) {
        log.info("Adding location with ID: {} to worker with ID: {}", locationId, workerId);
        try {
            Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
            Optional<Location> locationOpt = locationRepo.findById(locationId);

            if (workerOpt.isEmpty()) {
                log.warn("Worker with ID {} not found", workerId);
                throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
            }
            if (locationOpt.isEmpty()) {
                log.warn("Location with ID {} not found", locationId);
                throw new EntityNotFoundException("Location with id " + locationId + " not found");
            }

            ClientWorker worker = workerOpt.get();
            Location location = locationOpt.get();
            worker.setLocation(location);
            clientWorkerRepo.save(worker);

            log.info("Successfully added location with ID: {} to worker with ID: {}", locationId, workerId);
            return new ResponseDTO("Location to worker added successfully");
        } catch (Exception e) {
            log.error("Error while adding location with ID: {} to worker with ID: {}", locationId, workerId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO deleteWorker(Integer workerId) {
        log.info("Deleting worker with ID: {}", workerId);
        try {
            clientWorkerRepo.deleteById(workerId);
            log.info("Successfully deleted worker with ID: {}", workerId);
            return new ResponseDTO("Worker deleted successfully");
        } catch (Exception e) {
            log.error("Error while deleting worker with ID: {}", workerId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO updateClientWorker(Integer workerId, ClientWorkerDTO clientWorkerDTO) {
        log.info("Updating Client Worker with ID: {}", workerId);
        try {
            Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
            if (workerOpt.isEmpty()) {
                log.warn("Worker with ID {} not found", workerId);
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
            updateFavorite(worker, clientWorkerDTO);
            updateComment(worker, clientWorkerDTO);

            clientWorkerRepo.save(worker);
            log.info("Successfully updated Client Worker with ID: {}", workerId);
            return new ResponseDTO("Client worker updated successfully");
        } catch (Exception e) {
            log.error("Error while updating Client Worker with ID: {}", workerId, e);
            throw e;
        }
    }

    public void updateComment(ClientWorker worker, ClientWorkerDTO clientWorkerDTO) {
        if (clientWorkerDTO.comment() != null) {
            worker.setComment(clientWorkerDTO.comment());
        }
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

    public void updateFavorite(ClientWorker worker, ClientWorkerDTO clientWorkerDTO) {
        if (clientWorkerDTO.favorite() != null) {
            worker.setFavorite(clientWorkerDTO.favorite());
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
        log.info("Fetching all workers, ordered by favorite status.");
        try {
            List<ClientWorkerDTO> workers = clientWorkerMapper.toDtoList(clientWorkerRepo.findByOrderByFavoriteDesc());
            log.info("Fetched {} workers.", workers.size());
            return workers;
        } catch (Exception e) {
            log.error("Error while fetching all workers.", e);
            throw e;
        }
    }

    public List<ClientWorkerDTO> getWorkersByClientId(Integer clientId) {
        log.info("Fetching workers for client with ID: {}, ordered by favorite status.", clientId);
        try {
            List<ClientWorkerDTO> workers = clientWorkerMapper.toDtoList(clientWorkerRepo
                    .findByClientIdOrderByFavoriteDesc(clientId));
            log.info("Fetched {} workers for client with ID: {}", workers.size(), clientId);
            return workers;
        } catch (Exception e) {
            log.error("Error while fetching workers for client with ID: {}", clientId, e);
            throw e;
        }
    }

    public LocationDTO getWorkerLocation(Integer workerId) {
        log.info("Fetching location for worker with ID: {}", workerId);
        try {
            Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
            if (workerOpt.isEmpty()) {
                log.warn("Worker with ID {} not found.", workerId);
                throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
            }

            ClientWorker worker = workerOpt.get();
            Location location = worker.getLocation();
            LocationDTO locationDTO = locationMapper.toDto(location);
            log.info("Fetched location for worker with ID: {}", workerId);
            return locationDTO;
        } catch (Exception e) {
            log.error("Error while fetching location for worker with ID: {}", workerId, e);
            throw e;
        }
    }

    public ClientDTO getWorkerEmployer(Integer workerId) {
        log.info("Fetching employer for worker with ID: {}", workerId);
        try {
            Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
            if (workerOpt.isEmpty()) {
                log.warn("Worker with ID {} not found.", workerId);
                throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
            }

            ClientWorker worker = workerOpt.get();
            Client client = worker.getClient();
            ClientDTO clientDTO = clientMapper.toDto(client);
            log.info("Fetched employer for worker with ID: {}", workerId);
            return clientDTO;
        } catch (Exception e) {
            log.error("Error while fetching employer for worker with ID: {}", workerId, e);
            throw e;
        }
    }

    public Set<ClientWorker> contactIdsToClientWorkersSet(List<Integer> contactIds) {
        log.info("Converting contact IDs to ClientWorker set: {}", contactIds);
        try {
            Set<ClientWorker> contacts = new HashSet<>();
            for (Integer id : contactIds) {
                ClientWorker contact = clientWorkerRepo.findById(id)
                        .orElseThrow(() -> {
                            log.warn("Invalid worker ID: {}", id);
                            return new IllegalArgumentException("Invalid worker ID: " + id);
                        });
                contacts.add(contact);
            }
            log.info("Converted {} contact IDs to ClientWorker set.", contacts.size());
            return contacts;
        } catch (Exception e) {
            log.error("Error while converting contact IDs to ClientWorker set: {}", contactIds, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO toggleFavorite(Integer workerId) {
        log.info("Toggling favorite status for worker with ID: {}", workerId);
        try {
            Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
            if (workerOpt.isEmpty()) {
                log.warn("Worker with ID {} not found.", workerId);
                throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
            }

            ClientWorker worker = workerOpt.get();
            if (worker.getFavorite() == null) {
                worker.setFavorite(false);
            }
            worker.setFavorite(!worker.getFavorite());
            clientWorkerRepo.save(worker);
            log.info("Favorite status toggled for worker with ID: {}. New status: {}", workerId, worker.getFavorite());
            return new ResponseDTO("Worker favorite changed");
        } catch (Exception e) {
            log.error("Error while toggling favorite status for worker with ID: {}", workerId, e);
            throw e;
        }
    }

    public ResponseDTO removeClientFromWorker(Integer workerId) {
        log.info("Removing client association for worker with ID: {}", workerId);
        try {
            Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
            if (workerOpt.isEmpty()) {
                log.warn("Worker with ID {} not found.", workerId);
                throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
            }

            ClientWorker worker = workerOpt.get();
            worker.setClient(null);
            clientWorkerRepo.save(worker);
            log.info("Successfully removed client association for worker with ID: {}", workerId);
            return new ResponseDTO("Worker removed from client");
        } catch (Exception e) {
            log.error("Error while removing client association for worker with ID: {}", workerId, e);
            throw e;
        }
    }

    public List<ClientWorkerDTO> getNotUsedContacts() {
        log.info("Fetching all workers not associated with any client.");
        try {
            List<ClientWorkerDTO> workers = clientWorkerMapper.toDtoList(clientWorkerRepo.findByClientIsNull());
            log.info("Fetched {} workers not associated with any client.", workers.size());
            return workers;
        } catch (Exception e) {
            log.error("Error while fetching workers not associated with any client.", e);
            throw e;
        }
    }
}
