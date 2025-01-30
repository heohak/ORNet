package com.demo.bait.service.ClientServices;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.MaintenanceRepo;
import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ClientMaintenanceService {

    private ClientRepo clientRepo;
    private MaintenanceRepo maintenanceRepo;
    private MaintenanceMapper maintenanceMapper;
    private MaintenanceService maintenanceService;

    @Transactional
    public ResponseDTO addMaintenanceToClient(Integer clientId, Integer maintenanceId) {
        log.info("Adding Maintenance with ID: {} to Client with ID: {}", maintenanceId, clientId);
        try {
            Optional<Client> clientOpt = clientRepo.findById(clientId);
            Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);

            if (clientOpt.isEmpty()) {
                log.warn("Client with ID {} not found", clientId);
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }
            if (maintenanceOpt.isEmpty()) {
                log.warn("Maintenance with ID {} not found", maintenanceId);
                throw new EntityNotFoundException("Maintenance with id " + maintenanceId + " not found");
            }

            Client client = clientOpt.get();
            Maintenance maintenance = maintenanceOpt.get();

            log.debug("Adding Maintenance with ID: {} to Client with ID: {}", maintenanceId, clientId);
            client.getMaintenances().add(maintenance);
            clientRepo.save(client);

            log.info("Successfully added Maintenance with ID: {} to Client with ID: {}", maintenanceId, clientId);
            return new ResponseDTO("Maintenance added successfully");
        } catch (Exception e) {
            log.error("Error while adding Maintenance with ID: {} to Client with ID: {}", maintenanceId, clientId, e);
            throw e;
        }
    }

    public List<MaintenanceDTO> getClientMaintenances(Integer clientId) {
        if (clientId == null) {
            log.warn("Client ID is null. Returning empty list");
            return Collections.emptyList();
        }

        log.info("Fetching maintenances for Client with ID: {}", clientId);
        try {
            Optional<Client> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                log.warn("Client with ID {} not found", clientId);
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }

            Client client = clientOpt.get();
            List<MaintenanceDTO> maintenanceDTOs = maintenanceMapper
                    .toDtoList(client.getMaintenances().stream().toList());

            log.debug("Fetched {} maintenances for Client with ID: {}", maintenanceDTOs.size(), clientId);
            return maintenanceDTOs;
        } catch (Exception e) {
            log.error("Error while fetching maintenances for Client with ID: {}", clientId, e);
            throw e;
        }
    }

    public void updateMaintenances(Client client, ClientDTO clientDTO) {
        log.info("Updating maintenances for Client with ID: {}", client.getId());
        try {
            if (clientDTO.maintenanceIds() != null) {
                log.debug("Updating maintenances with IDs: {} for Client with ID: {}", clientDTO.maintenanceIds(), client.getId());
                Set<Maintenance> maintenances = maintenanceService
                        .maintenanceIdsToMaintenancesSet(clientDTO.maintenanceIds());
                client.setMaintenances(maintenances);
                log.info("Successfully updated maintenances for Client with ID: {}", client.getId());
            } else {
                log.debug("No maintenances provided to update for Client with ID: {}", client.getId());
            }
        } catch (Exception e) {
            log.error("Error while updating maintenances for Client with ID: {}", client.getId(), e);
            throw e;
        }
    }
}
