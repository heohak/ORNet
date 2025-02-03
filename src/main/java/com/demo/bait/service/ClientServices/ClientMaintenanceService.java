package com.demo.bait.service.ClientServices;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.mapper.FileUploadMapper;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.FileUploadRepo;
import com.demo.bait.repository.MaintenanceRepo;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private FileUploadRepo fileUploadRepo;
    private FileUploadService fileUploadService;
    private FileUploadMapper fileUploadMapper;

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

    @Transactional
    public ResponseDTO updateClientMaintenanceConditions(Integer clientId, ClientDTO clientDTO) {
        log.info("Starting updateMaintenanceConditions for Client ID: {}", clientId);

        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            log.error("Client with ID {} not found", clientId);
            throw new EntityNotFoundException("Client with ID " + clientId + " not found");
        }
        Client client = clientOpt.get();

        if (clientDTO.maintenanceDescription() != null) {
            log.debug("Updating maintenance description for Client ID: {}", clientId);
            client.setMaintenanceDescription(clientDTO.maintenanceDescription());
        }

        if (clientDTO.contractTermsId() != null) {
            log.debug("Updating contract terms for Client ID: {} using File ID: {}", clientId, clientDTO.contractTermsId());
            FileUpload file = fileUploadRepo.findById(clientDTO.contractTermsId())
                    .orElseThrow(() -> {
                        log.warn("Invalid File ID: {} provided for Client ID: {}", clientDTO.contractTermsId(), clientId);
                        return new IllegalArgumentException("Invalid file ID: " + clientDTO.contractTermsId());
                    });
            client.setContractTerms(file);
        }

        log.info("Client maintenance conditions updated successfully for Client ID: {}", clientId);
        return new ResponseDTO("Client maintenance conditions updated successfully");
    }

    @Transactional
    public ResponseDTO uploadContractTermsForClient(Integer clientId, MultipartFile file) {
        log.info("Uploading contract terms file for client with ID: {}", clientId);
        try {
            Optional<Client> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                log.warn("Client with ID {} not found.", clientId);
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }

            Client client = clientOpt.get();
            log.debug("Processing contract terms file for client with ID: {}", clientId);

            FileUpload uploadedFile = fileUploadService.uploadFile(file);
            client.setContractTerms(uploadedFile);
            clientRepo.save(client);

            log.info("Successfully uploaded contract terms file for client with ID: {}", clientId);
            return new ResponseDTO("Contract terms file uploaded successfully for client");
        } catch (IOException e) {
            log.error("IO Exception occurred while uploading contract terms file for client with ID: {}", clientId, e);
            throw new RuntimeException("Error uploading contract terms file", e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while uploading contract terms file for client with ID: {}", clientId, e);
            throw e;
        }
    }

    public FileUploadDTO getClientContractTerms(Integer clientId) {
        if (clientId == null) {
            log.warn("Client ID is null. Returning null");
            return null;
        }

        log.info("Fetching client contract terms with client ID: {}", clientId);
        try {
            Optional<Client> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                log.warn("Client with ID {} not found.", clientId);
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }

            Client client = clientOpt.get();
            FileUpload contractTerms = client.getContractTerms();
            return fileUploadMapper.toDto(contractTerms);
        } catch (Exception e) {
            log.error("Error while fetching contract terms for client with ID: {}", clientId, e);
            throw e;
        }
    }

    public ResponseDTO getClientMaintenanceDescription(Integer clientId) {
        if (clientId == null) {
            log.warn("Client ID is null. Returning null");
            return null;
        }
        log.info("Fetching client maintenance description with client ID: {}", clientId);
        try {
            Optional<Client> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                log.warn("Client with ID {} not found.", clientId);
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }
            Client client = clientOpt.get();

            String maintenanceDescription = client.getMaintenanceDescription();
            return new ResponseDTO(maintenanceDescription);
        } catch (Exception e) {
            log.error("Error while fetching maintenance description for client with ID: {}", clientId, e);
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
