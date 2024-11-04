package com.demo.bait.service.ClientServices;

import com.demo.bait.dto.*;
import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.ClientMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.LocationRepo;
import com.demo.bait.repository.MaintenanceRepo;
import com.demo.bait.repository.ThirdPartyITRepo;
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

import java.util.*;

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

    @Transactional
    public ResponseDTO addClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setFullName(clientDTO.fullName());
        client.setShortName(clientDTO.shortName());

        clientLocationService.updateLocations(client, clientDTO);

        clientThirdPartyITService.updateThirdPartyITs(client, clientDTO);

        client.setPathologyClient(Boolean.TRUE.equals(clientDTO.pathologyClient()));
        client.setSurgeryClient(Boolean.TRUE.equals(clientDTO.surgeryClient()));
        client.setEditorClient(Boolean.TRUE.equals(clientDTO.editorClient()));
        client.setOtherMedicalDevices(Boolean.TRUE.equals(clientDTO.otherMedicalDevices()));
        client.setProspect(Boolean.TRUE.equals(clientDTO.prospect()));
        client.setAgreement(Boolean.TRUE.equals(clientDTO.agreement()));

        client.setLastMaintenance(clientDTO.lastMaintenance());
        client.setNextMaintenance(clientDTO.nextMaintenance());

        clientMaintenanceService.updateMaintenances(client, clientDTO);

        clientRepo.save(client);
        return new ResponseDTO(client.getId().toString());
    }

    @Transactional
    public ResponseDTO deleteClient(Integer id) {
        clientRepo.deleteById(id);
        return new ResponseDTO("Client deleted successfully");
    }

    @Transactional
    public ResponseDTO updateClient(Integer clientId, ClientDTO clientDTO) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }
        Client client = clientOpt.get();

        updateFullName(client, clientDTO);
        updateShortName(client, clientDTO);
        clientLocationService.updateLocations(client, clientDTO);
        clientThirdPartyITService.updateThirdPartyITs(client, clientDTO);
        updatePathologyClient(client, clientDTO);
        updateSurgeryClient(client, clientDTO);
        updateEditorClient(client, clientDTO);
        updateOtherMedicalDevices(client, clientDTO);
        updateProspect(client, clientDTO);
        updateAgreement(client, clientDTO);
        updateLastMaintenance(client, clientDTO);
        updateNextMaintenance(client, clientDTO);
        clientMaintenanceService.updateMaintenances(client, clientDTO);

        clientRepo.save(client);
        return new ResponseDTO("Client updated successfully");
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
        return clientMapper.toDtoList(clientRepo.findAll());
    }

    public ClientDTO getClientById(Integer clientId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }
        return clientMapper.toDto(clientOpt.get());
    }

    public List<ClientDTO> getClientHistory(Integer clientId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(Client.class, clientId);

        List<Client> history = new ArrayList<>();
        for (Number rev : revisions) {
            Client clientVersion = auditReader
                    .find(Client.class, clientId, rev);
            history.add(clientVersion);
        }
        return clientMapper.toDtoList(history);
    }
}
