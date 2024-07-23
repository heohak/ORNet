package com.demo.bait.service;

import com.demo.bait.dto.*;
import com.demo.bait.entity.*;
import com.demo.bait.mapper.ClientMapper;
import com.demo.bait.mapper.LocationMapper;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.mapper.ThirdPartyITMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.LocationRepo;
import com.demo.bait.repository.MaintenanceRepo;
import com.demo.bait.repository.ThirdPartyITRepo;
import com.demo.bait.specification.ClientSpecification;
import com.demo.bait.specification.TicketSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ClientService {

    private ClientRepo clientRepo;
    private ClientMapper clientMapper;
    private LocationRepo locationRepo;
    private LocationMapper locationMapper;
    private ThirdPartyITRepo thirdPartyITRepo;
    private ThirdPartyITMapper thirdPartyITMapper;
    private MaintenanceRepo maintenanceRepo;
    private MaintenanceMapper maintenanceMapper;


    public ResponseDTO addClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setFullName(clientDTO.fullName());
        client.setShortName(clientDTO.shortName());

        if (clientDTO.locationIds() != null) {
            Set<Location> locations = new HashSet<>();
            for (Integer locationId : clientDTO.locationIds()) {
                Location location = locationRepo.findById(locationId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid location ID: " + locationId));
                locations.add(location);
            }
            client.setLocations(locations);
        }

        if (clientDTO.thirdPartyIds() != null) {
            Set<ThirdPartyIT> thirdPartyITs = new HashSet<>();
            for (Integer thirdPartyITId : clientDTO.thirdPartyIds()) {
                ThirdPartyIT thirdPartyIT = thirdPartyITRepo.findById(thirdPartyITId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid third party IT ID: " + thirdPartyITId));
                thirdPartyITs.add(thirdPartyIT);
            }
            client.setThirdPartyITs(thirdPartyITs);
        }

        if (clientDTO.pathologyClient() == null) {
            client.setPathologyClient(false);
        } else {
            client.setPathologyClient(clientDTO.pathologyClient());
        }

        if (clientDTO.surgeryClient() == null) {
            client.setSurgeryClient(false);
        } else {
            client.setSurgeryClient(clientDTO.surgeryClient());
        }

        if (clientDTO.editorClient() == null) {
            client.setEditorClient(false);
        } else {
            client.setEditorClient(clientDTO.editorClient());
        }

        client.setOtherMedicalInformation(clientDTO.otherMedicalInformation());
        client.setLastMaintenance(clientDTO.lastMaintenance());
        client.setNextMaintenance(clientDTO.nextMaintenance());

        if (clientDTO.maintenanceIds() != null) {
            Set<Maintenance> maintenances = new HashSet<>();
            for (Integer maintenanceId : clientDTO.maintenanceIds()) {
                Maintenance maintenance = maintenanceRepo.findById(maintenanceId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance ID: " + maintenanceId));
                maintenances.add(maintenance);
            }
            client.setMaintenances(maintenances);
        }

        clientRepo.save(client);
        return new ResponseDTO(client.getId().toString());
    }

    public List<ClientDTO> getAllClients() {
        return clientMapper.toDtoList(clientRepo.findAll());
    }

    public ResponseDTO deleteClient(Integer id) {
        clientRepo.deleteById(id);
        return new ResponseDTO("Client deleted successfully");
    }

    @Transactional
    public ResponseDTO addLocationToClient(Integer clientId, Integer locationId) {
        Optional<Location> locationOpt = locationRepo.findById(locationId);
        Optional<Client> clientOpt = clientRepo.findById(clientId);

        if (locationOpt.isEmpty()) {
            throw new EntityNotFoundException("Location with id " + locationId + "not found");
        }
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }

        Client client = clientOpt.get();
        Location location = locationOpt.get();
        client.getLocations().add(location);
        clientRepo.save(client);
        return new ResponseDTO("Location added successfully");
    }

    @Transactional
    public ResponseDTO addThirdPartyIT(Integer clientId, Integer thirdPartyITId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        Optional<ThirdPartyIT> thirdPartyITOpt = thirdPartyITRepo.findById(thirdPartyITId);

        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }
        if (thirdPartyITOpt.isEmpty()) {
            throw new EntityNotFoundException("Third party with id " + thirdPartyITId + " not found");
        }

        Client client = clientOpt.get();
        ThirdPartyIT thirdPartyIT = thirdPartyITOpt.get();
        client.getThirdPartyITs().add(thirdPartyIT);
        clientRepo.save(client);
        return new ResponseDTO("Third party added successfully");
    }

    @Transactional
    public ResponseDTO addMaintenanceToClient(Integer clientId, Integer maintenanceId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);

        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }
        if (maintenanceOpt.isEmpty()) {
            throw new EntityNotFoundException("Maintenance with id " + maintenanceId + " not found");
        }

        Client client = clientOpt.get();
        Maintenance maintenance = maintenanceOpt.get();
        client.getMaintenances().add(maintenance);
        clientRepo.save(client);
        return new ResponseDTO("Maintenance added successfully");
    }

    public List<LocationDTO> getClientLocations(Integer clientId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }

        Client client = clientOpt.get();
        return locationMapper.toDtoList(client.getLocations().stream().toList());
    }

    public List<ThirdPartyITDTO> getClientThirdPartyITs(Integer clientId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }

        Client client = clientOpt.get();
        return thirdPartyITMapper.toDtoList(client.getThirdPartyITs().stream().toList());
    }

    public List<MaintenanceDTO> getClientMaintenances(Integer clientId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }

        Client client = clientOpt.get();
        return maintenanceMapper.toDtoList(client.getMaintenances().stream().toList());
    }

    public ClientDTO getClientById(Integer clientId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }
        return clientMapper.toDto(clientOpt.get());
    }

    public List<ClientDTO> searchClients(String searchTerm) {
        Specification<Client> spec = new ClientSpecification(searchTerm);
        return clientMapper.toDtoList(clientRepo.findAll(spec));
    }

    public List<ClientDTO> findClientsByType(String clientType) {
        Specification<Client> spec = ClientSpecification.hasClientType(clientType);
        return clientMapper.toDtoList(clientRepo.findAll(spec));
    }

    public List<ClientDTO> searchAndFilterClients(String searchTerm, String clientType) {
        Specification<Client> searchSpec = new ClientSpecification(searchTerm);
        Specification<Client> statusSpec = ClientSpecification.hasClientType(clientType);
        Specification<Client> combinedSpec = Specification.where(searchSpec).and(statusSpec);
        return clientMapper.toDtoList(clientRepo.findAll(combinedSpec));
    }
}
