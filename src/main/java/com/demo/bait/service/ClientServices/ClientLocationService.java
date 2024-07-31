package com.demo.bait.service.ClientServices;

import com.demo.bait.dto.LocationDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Location;
import com.demo.bait.mapper.LocationMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.LocationRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ClientLocationService {

    private ClientRepo clientRepo;
    private LocationRepo locationRepo;
    private LocationMapper locationMapper;

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
