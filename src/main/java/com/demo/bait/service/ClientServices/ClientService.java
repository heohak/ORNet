package com.demo.bait.service.ClientServices;

import com.demo.bait.dto.*;
import com.demo.bait.entity.*;
import com.demo.bait.mapper.ClientMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.LocationRepo;
import com.demo.bait.repository.MaintenanceRepo;
import com.demo.bait.repository.ThirdPartyITRepo;
import com.demo.bait.service.LocationServices.LocationService;
import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import com.demo.bait.service.ThirdPartyITServices.ThirdPartyITService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private MaintenanceService maintenanceService;
    private LocationService locationService;
    private ThirdPartyITService thirdPartyITService;

    @Transactional
    public ResponseDTO addClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setFullName(clientDTO.fullName());
        client.setShortName(clientDTO.shortName());

        if (clientDTO.locationIds() != null) {
            Set<Location> locations = locationService.locationIdsToLocationsSet(clientDTO.locationIds());
            client.setLocations(locations);
        }

        if (clientDTO.thirdPartyIds() != null) {
            Set<ThirdPartyIT> thirdPartyITs = thirdPartyITService.thirdPartyITIdsToThirdPartyITsSet(clientDTO.thirdPartyIds());
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
            Set<Maintenance> maintenances = maintenanceService.maintenanceIdsToMaintenancesSet(clientDTO.maintenanceIds());
            client.setMaintenances(maintenances);
        }

        clientRepo.save(client);
        return new ResponseDTO(client.getId().toString());
    }

    @Transactional
    public ResponseDTO deleteClient(Integer id) {
        clientRepo.deleteById(id);
        return new ResponseDTO("Client deleted successfully");
    }

    public List<ClientDTO> getAllClients() {
        return clientMapper.toDtoList(clientRepo.findAll());
    }

    public ClientDTO getClientById(Integer clientId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }
        return clientMapper.toDto(clientOpt.get());
    }
}
