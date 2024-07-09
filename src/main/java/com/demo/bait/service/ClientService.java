package com.demo.bait.service;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Location;
import com.demo.bait.mapper.ClientMapper;
import com.demo.bait.mapper.LocationMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.LocationRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    public ResponseDTO addClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setFullName(clientDTO.fullName());
        client.setShortName(clientDTO.shortName());
        client.setThirdPartyIT(clientDTO.thirdPartyIT());

        if (clientDTO.locationIds() != null) {
            Set<Location> locations = new HashSet<>();
            for (Integer locationId : clientDTO.locationIds()) {
                Location location = locationRepo.findById(locationId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid location ID: " + locationId));
                locations.add(location);
            }
            client.setLocations(locations);
        }

        clientRepo.save(client);
        return new ResponseDTO("Client added successfully");
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

    public List<LocationDTO> getClientLocations(Integer clientId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }

        Client client = clientOpt.get();
        return locationMapper.toDtoList(client.getLocations().stream().toList());
    }
}
