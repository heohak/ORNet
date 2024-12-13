package com.demo.bait.service.ClientServices;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.ThirdPartyIT;
import com.demo.bait.mapper.ThirdPartyITMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.ThirdPartyITRepo;
import com.demo.bait.service.ThirdPartyITServices.ThirdPartyITService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ClientThirdPartyITService {

    private ClientRepo clientRepo;
    private ThirdPartyITRepo thirdPartyITRepo;
    private ThirdPartyITMapper thirdPartyITMapper;
    private ThirdPartyITService thirdPartyITService;

    @Transactional
    public ResponseDTO addThirdPartyIT(Integer clientId, Integer thirdPartyITId) {
        log.info("Adding Third Party IT with ID: {} to Client with ID: {}", thirdPartyITId, clientId);
        try {
            Optional<Client> clientOpt = clientRepo.findById(clientId);
            Optional<ThirdPartyIT> thirdPartyITOpt = thirdPartyITRepo.findById(thirdPartyITId);

            if (clientOpt.isEmpty()) {
                log.warn("Client with ID {} not found", clientId);
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }
            if (thirdPartyITOpt.isEmpty()) {
                log.warn("Third Party IT with ID {} not found", thirdPartyITId);
                throw new EntityNotFoundException("Third party with id " + thirdPartyITId + " not found");
            }

            Client client = clientOpt.get();
            ThirdPartyIT thirdPartyIT = thirdPartyITOpt.get();

            log.debug("Adding Third Party IT with ID: {} to Client with ID: {}", thirdPartyITId, clientId);
            client.getThirdPartyITs().add(thirdPartyIT);
            clientRepo.save(client);

            log.info("Successfully added Third Party IT with ID: {} to Client with ID: {}", thirdPartyITId, clientId);
            return new ResponseDTO("Third party added successfully");
        } catch (Exception e) {
            log.error("Error while adding Third Party IT with ID: {} to Client with ID: {}", thirdPartyITId, clientId, e);
            throw e;
        }
    }

    public List<ThirdPartyITDTO> getClientThirdPartyITs(Integer clientId) {
        log.info("Fetching Third Party ITs for Client with ID: {}", clientId);
        try {
            Optional<Client> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                log.warn("Client with ID {} not found", clientId);
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }

            Client client = clientOpt.get();
            List<ThirdPartyITDTO> thirdPartyITDTOs = thirdPartyITMapper.toDtoList(client.getThirdPartyITs().stream().toList());

            log.debug("Fetched {} Third Party ITs for Client with ID: {}", thirdPartyITDTOs.size(), clientId);
            return thirdPartyITDTOs;
        } catch (Exception e) {
            log.error("Error while fetching Third Party ITs for Client with ID: {}", clientId, e);
            throw e;
        }
    }

    public void updateThirdPartyITs(Client client, ClientDTO clientDTO) {
        log.info("Updating Third Party ITs for Client with ID: {}", client.getId());
        try {
            if (clientDTO.thirdPartyIds() != null) {
                log.debug("Updating Third Party ITs with IDs: {} for Client with ID: {}", clientDTO.thirdPartyIds(), client.getId());
                Set<ThirdPartyIT> thirdPartyITs = thirdPartyITService.thirdPartyITIdsToThirdPartyITsSet(clientDTO.thirdPartyIds());
                client.setThirdPartyITs(thirdPartyITs);
                log.info("Successfully updated Third Party ITs for Client with ID: {}", client.getId());
            } else {
                log.debug("No Third Party ITs provided to update for Client with ID: {}", client.getId());
            }
        } catch (Exception e) {
            log.error("Error while updating Third Party ITs for Client with ID: {}", client.getId(), e);
            throw e;
        }
    }
}
