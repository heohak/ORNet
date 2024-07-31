package com.demo.bait.service.ClientServices;

import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.MaintenanceRepo;
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
public class ClientMaintenanceService {

    private ClientRepo clientRepo;
    private MaintenanceRepo maintenanceRepo;
    private MaintenanceMapper maintenanceMapper;

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

    public List<MaintenanceDTO> getClientMaintenances(Integer clientId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }

        Client client = clientOpt.get();
        return maintenanceMapper.toDtoList(client.getMaintenances().stream().toList());
    }
}
