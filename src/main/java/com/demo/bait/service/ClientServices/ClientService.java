package com.demo.bait.service.ClientServices;

import com.demo.bait.dto.*;
import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.ClientActivityMapper;
import com.demo.bait.mapper.ClientMapper;
import com.demo.bait.repository.*;
import com.demo.bait.service.LocationServices.LocationService;
import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import com.demo.bait.service.ThirdPartyITServices.ThirdPartyITService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ClientService {

    private ClientRepo clientRepo;
    private ClientMapper clientMapper;
    private ClientLocationService clientLocationService;
    private ClientThirdPartyITService clientThirdPartyITService;
    private ClientMaintenanceService clientMaintenanceService;
    private EntityManager entityManager;
    private ClientActivityRepo clientActivityRepo;
    private ClientActivityMapper clientActivityMapper;
    private ClientWorkerRepo clientWorkerRepo;
    private TicketRepo ticketRepo;
    private DeviceRepo deviceRepo;
    private SoftwareRepo softwareRepo;

    @Transactional
    public ResponseDTO addClient(ClientDTO clientDTO) {
        log.info("Adding a new Client: {}", clientDTO);
        try {
            Client client = new Client();
            client.setFullName(clientDTO.fullName());
            client.setShortName(clientDTO.shortName());
            client.setCountry(clientDTO.country());

            clientLocationService.updateLocations(client, clientDTO);
            clientThirdPartyITService.updateThirdPartyITs(client, clientDTO);

            client.setPathologyClient(Boolean.TRUE.equals(clientDTO.pathologyClient()));
            client.setSurgeryClient(Boolean.TRUE.equals(clientDTO.surgeryClient()));
            client.setEditorClient(Boolean.TRUE.equals(clientDTO.editorClient()));
            client.setOtherMedicalDevices(Boolean.TRUE.equals(clientDTO.otherMedicalDevices()));
            client.setProspect(Boolean.TRUE.equals(clientDTO.prospect()));
            client.setAgreement(Boolean.TRUE.equals(clientDTO.agreement()));
            client.setActiveCustomer(Boolean.TRUE.equals(clientDTO.activeCustomer()));

            client.setLastMaintenance(clientDTO.lastMaintenance());
            client.setNextMaintenance(clientDTO.nextMaintenance());

            clientMaintenanceService.updateMaintenances(client, clientDTO);

            clientRepo.save(client);
            log.info("Successfully added Client with ID: {}", client.getId());
            return new ResponseDTO(client.getId().toString());
        } catch (Exception e) {
            log.error("Error while adding Client: {}", clientDTO, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO deleteClient(Integer id) {
        log.info("Deleting Client with ID: {}", id);

        Optional<Client> clientOpt = clientRepo.findById(id);
        if (clientOpt.isEmpty()) {
            log.error("Client with ID {} not found", id);
            throw new EntityNotFoundException("Client with ID " + id + " not found");
        }
        Client client = clientOpt.get();

        try {
            List<ClientWorker> clientWorkers = clientWorkerRepo.findAllByClient(client);
            for (ClientWorker worker : clientWorkers) {
                worker.setClient(null);
                worker.setLocation(null);
                worker.getRoles().clear();
                clientWorkerRepo.save(worker);
            }

            List<Ticket> tickets = ticketRepo.findAllByClient(client);
            for (Ticket ticket : tickets) {
                ticket.setClient(null);
                ticketRepo.save(ticket);
            }

            List<Device> devices = deviceRepo.findAllByClient(client);
            for (Device device : devices) {
                device.setClient(null);
                device.setLocation(null);
                deviceRepo.save(device);
            }

            List<Software> softwareList = softwareRepo.findAllByClient(client);
            for (Software software : softwareList) {
                software.setClient(null);
                softwareRepo.save(software);
            }

            List<ClientActivity> clientActivities = clientActivityRepo.findAllByClient(client);
            for (ClientActivity activity : clientActivities) {
                activity.setClient(null);
                activity.setLocation(null);
                activity.getContacts().clear();
                activity.getWorkTypes().clear();
                activity.getFiles().clear();
                activity.getDevices().clear();
                clientActivityRepo.delete(activity);
            }

            client.getLocations().clear();
            client.getThirdPartyITs().clear();
            client.getMaintenances().clear();

            clientRepo.save(client);

            clientRepo.delete(client);
            log.info("Successfully deleted Client with ID: {}", id);
            return new ResponseDTO("Client deleted successfully");
        } catch (Exception e) {
            log.error("Error while deleting Client with ID: {}", id, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO updateClient(Integer clientId, ClientDTO clientDTO) {
        log.info("Updating Client with ID: {}", clientId);
        try {
            Optional<Client> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                log.warn("Client with ID {} not found", clientId);
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }
            Client client = clientOpt.get();

            updateFullName(client, clientDTO);
            updateShortName(client, clientDTO);
            updateCountry(client, clientDTO);
            clientLocationService.updateLocations(client, clientDTO);
            clientThirdPartyITService.updateThirdPartyITs(client, clientDTO);
            updatePathologyClient(client, clientDTO);
            updateSurgeryClient(client, clientDTO);
            updateEditorClient(client, clientDTO);
            updateOtherMedicalDevices(client, clientDTO);
            updateProspect(client, clientDTO);
            updateAgreement(client, clientDTO);
            updateActiveCustomer(client, clientDTO);
            updateLastMaintenance(client, clientDTO);
            updateNextMaintenance(client, clientDTO);
            clientMaintenanceService.updateMaintenances(client, clientDTO);

            clientRepo.save(client);
            log.info("Successfully updated Client with ID: {}", clientId);
            return new ResponseDTO("Client updated successfully");
        } catch (Exception e) {
            log.error("Error while updating Client with ID: {}", clientId, e);
            throw e;
        }
    }

    public void updateFullName(Client client, ClientDTO clientDTO) {
        if (clientDTO.fullName() != null) {
            client.setFullName(clientDTO.fullName());
        }
    }

    public void updateShortName(Client client, ClientDTO clientDTO) {
        if (clientDTO.shortName() != null) {
            client.setShortName(clientDTO.shortName());
        }
    }

    public void updateCountry(Client client, ClientDTO clientDTO) {
        if (clientDTO.country() != null) {
            client.setCountry(clientDTO.country());
        }
    }

    public void updatePathologyClient(Client client, ClientDTO clientDTO) {
        if (clientDTO.pathologyClient() != null) {
            client.setPathologyClient(clientDTO.pathologyClient());
        }
    }

    public void updateSurgeryClient(Client client, ClientDTO clientDTO) {
        if (clientDTO.surgeryClient() != null) {
            client.setSurgeryClient(clientDTO.surgeryClient());
        }
    }

    public void updateEditorClient(Client client, ClientDTO clientDTO) {
        if (clientDTO.editorClient() != null) {
            client.setEditorClient(clientDTO.editorClient());
        }
    }

    public void updateOtherMedicalDevices(Client client, ClientDTO clientDTO) {
        if (clientDTO.otherMedicalDevices() != null) {
            client.setOtherMedicalDevices(clientDTO.otherMedicalDevices());
        }
    }

    public void updateProspect(Client client, ClientDTO clientDTO) {
        if (clientDTO.prospect() != null) {
            client.setProspect(clientDTO.prospect());
        }
    }

    public void updateAgreement(Client client, ClientDTO clientDTO) {
        if (clientDTO.agreement() != null) {
            client.setAgreement(clientDTO.agreement());
        }
    }

    public void updateActiveCustomer(Client client, ClientDTO clientDTO) {
        if (clientDTO.activeCustomer() != null) {
            client.setActiveCustomer(clientDTO.activeCustomer());
        }
    }

    public void updateLastMaintenance(Client client, ClientDTO clientDTO) {
        if (clientDTO.lastMaintenance() != null) {
            client.setLastMaintenance(clientDTO.lastMaintenance());
        }
    }

    public void updateNextMaintenance(Client client, ClientDTO clientDTO) {
        if (clientDTO.nextMaintenance() != null) {
            client.setNextMaintenance(clientDTO.nextMaintenance());
        }
    }

    public List<ClientDTO> getAllClients() {
        log.info("Fetching all Clients");
        try {
            List<ClientDTO> clients = clientMapper.toDtoList(clientRepo.findAll());
            log.info("Fetched {} Clients", clients.size());
            return clients;
        } catch (Exception e) {
            log.error("Error while fetching all Clients", e);
            throw e;
        }
    }

    public ClientDTO getClientById(Integer clientId) {
        log.info("Fetching Client with ID: {}", clientId);
        try {
            Optional<Client> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                log.warn("Client with ID {} not found", clientId);
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }
            ClientDTO clientDTO = clientMapper.toDto(clientOpt.get());
            log.info("Fetched Client: {}", clientDTO);
            return clientDTO;
        } catch (Exception e) {
            log.error("Error while fetching Client with ID: {}", clientId, e);
            throw e;
        }
    }

    public List<ClientDTO> getClientHistory(Integer clientId) {
        log.info("Fetching history for Client with ID: {}", clientId);
        try {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            List<Number> revisions = auditReader.getRevisions(Client.class, clientId);

            List<Client> history = new ArrayList<>();
            for (Number rev : revisions) {
                Client clientVersion = auditReader.find(Client.class, clientId, rev);
                history.add(clientVersion);
            }
            List<ClientDTO> clientHistory = clientMapper.toDtoList(history);
            log.debug("Fetched history for Client with ID {}: {}", clientId, clientHistory);
            return clientHistory;
        } catch (Exception e) {
            log.error("Error while fetching history for Client with ID: {}", clientId, e);
            throw e;
        }
    }

    public List<ClientActivityDTO> getClientActivitiesForClient(Integer clientId) {
        log.info("Fetching Client Activities for Client with ID: {}", clientId);
        try {
            List<ClientActivityDTO> activities = clientActivityMapper.toDtoList(clientActivityRepo.findByClientId(clientId));
            log.info("Fetched {} Client Activities for Client with ID: {}", activities.size(), clientId);
            return activities;
        } catch (Exception e) {
            log.error("Error while fetching Client Activities for Client with ID: {}", clientId, e);
            throw e;
        }
    }

    public List<String> getAllClientCountries() {
        log.info("Fetching all unique Client countries");
        try {
            List<String> countries = clientRepo.findAll().stream()
                    .map(Client::getCountry)
                    .filter(country -> country != null && !country.isEmpty())
                    .map(country -> country.substring(0, 1).toUpperCase() + country.substring(1).toLowerCase())
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            log.info("Fetched {} unique Client countries", countries.size());
            return countries;
        } catch (Exception e) {
            log.error("Error while fetching all Client countries", e);
            throw e;
        }
    }

    public Map<Integer, Map<String, LocalDateTime>> getClientsActivityDates() {
        log.info("Fetching Client Activity dates");
        try {
            Map<Integer, Map<String, LocalDateTime>> clientActivityDates = new HashMap<>();
            LocalDateTime today = LocalDateTime.now();

            List<ClientActivity> activities = clientActivityRepo.findAll();

            Map<Integer, List<ClientActivity>> activitiesByClient = activities.stream()
                    .filter(activity -> activity.getClient() != null)
                    .collect(Collectors.groupingBy(activity -> activity.getClient().getId()));

            for (Map.Entry<Integer, List<ClientActivity>> entry : activitiesByClient.entrySet()) {
                Integer clientId = entry.getKey();
                List<ClientActivity> clientActivities = entry.getValue();

                LocalDateTime endDateTime = clientActivities.stream()
                        .map(ClientActivity::getEndDateTime)
                        .filter(Objects::nonNull)
                        .reduce((closest, current) -> {
                            if (closest == null) {
                                return current;
                            }
                            if (current.isBefore(today) && closest.isBefore(today)) {
                                return current.isBefore(closest) ? current : closest;
                            } else if (current.isBefore(today)) {
                                return current;
                            } else if (closest.isBefore(today)) {
                                return closest;
                            } else {
                                return current.isBefore(closest) ? current : closest;
                            }
                        })
                        .orElse(null);

                LocalDateTime updateDateTime = clientActivities.stream()
                        .map(ClientActivity::getUpdateDateTime)
                        .filter(Objects::nonNull)
                        .max(LocalDateTime::compareTo)
                        .orElse(null);

                Map<String, LocalDateTime> dateMap = new HashMap<>();
                dateMap.put("endDateTime", endDateTime);
                dateMap.put("updateDateTime", updateDateTime);

                clientActivityDates.put(clientId, dateMap);
            }

            log.info("Fetched activity dates for {} Clients", clientActivityDates.size());
            return clientActivityDates;
        } catch (Exception e) {
            log.error("Error while fetching Client Activity dates", e);
            throw e;
        }
    }
}
