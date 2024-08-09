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

    public List<ThirdPartyITDTO> getClientThirdPartyITs(Integer clientId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }

        Client client = clientOpt.get();
        return thirdPartyITMapper.toDtoList(client.getThirdPartyITs().stream().toList());
    }

    public void updateThirdPartyITs(Client client, ClientDTO clientDTO) {
        if (clientDTO.thirdPartyIds() != null) {
            Set<ThirdPartyIT> thirdPartyITs = thirdPartyITService
                    .thirdPartyITIdsToThirdPartyITsSet(clientDTO.thirdPartyIds());
            client.setThirdPartyITs(thirdPartyITs);
        }
    }
}
