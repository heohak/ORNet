package com.demo.bait.service.ClientServices;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Location;
import com.demo.bait.mapper.LocationMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.LocationRepo;
import com.demo.bait.service.LocationServices.LocationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class ClientLocationService {

    private ClientRepo clientRepo;
    private LocationRepo locationRepo;
    private LocationMapper locationMapper;
    private LocationService locationService;

    @Transactional
    public ResponseDTO addLocationToClient(Integer clientId, Integer locationId) {
        log.info("Adding Location with ID: {} to Client with ID: {}", locationId, clientId);
        try {
            Optional<Location> locationOpt = locationRepo.findById(locationId);
            Optional<Client> clientOpt = clientRepo.findById(clientId);

            if (locationOpt.isEmpty()) {
                log.warn("Location with ID {} not found", locationId);
                throw new EntityNotFoundException("Location with id " + locationId + " not found");
            }
            if (clientOpt.isEmpty()) {
                log.warn("Client with ID {} not found", clientId);
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }

            Client client = clientOpt.get();
            Location location = locationOpt.get();

            log.debug("Adding Location with ID: {} to Client with ID: {}", locationId, clientId);
            client.getLocations().add(location);
            clientRepo.save(client);

            log.info("Successfully added Location with ID: {} to Client with ID: {}", locationId, clientId);
            return new ResponseDTO("Location added successfully");
        } catch (Exception e) {
            log.error("Error while adding Location with ID: {} to Client with ID: {}", locationId, clientId, e);
            throw e;
        }
    }

    public List<LocationDTO> getClientLocations(Integer clientId) {
        if (clientId == null) {
            log.warn("Client ID is null. Returning empty list");
            return Collections.emptyList();
        }

        log.info("Fetching locations for Client with ID: {}", clientId);
        try {
            Optional<Client> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                log.warn("Client with ID {} not found", clientId);
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }

            Client client = clientOpt.get();
            List<LocationDTO> locationDTOs = locationMapper.toDtoList(client.getLocations().stream().toList());

            log.debug("Fetched {} locations for Client with ID: {}", locationDTOs.size(), clientId);
            return locationDTOs;
        } catch (Exception e) {
            log.error("Error while fetching locations for Client with ID: {}", clientId, e);
            throw e;
        }
    }

    public void updateLocations(Client client, ClientDTO clientDTO) {
        log.info("Updating locations for Client with ID: {}", client.getId());
        try {
            if (clientDTO.locationIds() != null) {
                log.debug("Updating locations with IDs: {} for Client with ID: {}", clientDTO.locationIds(), client.getId());
                Set<Location> locations = locationService.locationIdsToLocationsSet(clientDTO.locationIds());
                client.setLocations(locations);
                log.info("Successfully updated locations for Client with ID: {}", client.getId());
            } else {
                log.debug("No locations provided to update for Client with ID: {}", client.getId());
            }
        } catch (Exception e) {
            log.error("Error while updating locations for Client with ID: {}", client.getId(), e);
            throw e;
        }
    }
}
