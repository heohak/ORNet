package com.demo.bait.service.ThirdPartyITServices;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.ThirdPartyIT;
import com.demo.bait.mapper.ThirdPartyITMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.ThirdPartyITRepo;
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
public class ThirdPartyITService {

    private ThirdPartyITRepo thirdPartyITRepo;
    private ThirdPartyITMapper thirdPartyITMapper;
    private ClientRepo clientRepo;

    @Transactional
    public ThirdPartyITDTO addThirdPartyIT(ThirdPartyITDTO thirdPartyITDTO) {
        log.info("Adding a new Third Party IT with name: {}", thirdPartyITDTO.name());
        ThirdPartyIT thirdPartyIT = new ThirdPartyIT();
        thirdPartyIT.setName(thirdPartyITDTO.name());
        thirdPartyIT.setEmail(thirdPartyITDTO.email());
        thirdPartyIT.setPhone(thirdPartyITDTO.phone());
        ThirdPartyIT savedThirdParty = thirdPartyITRepo.save(thirdPartyIT);
        log.info("Third Party IT added successfully with ID: {}", savedThirdParty.getId());
        return thirdPartyITMapper.toDto(savedThirdParty);
    }

    @Transactional
    public ResponseDTO deleteThirdPartyIT(Integer id) {
        log.info("Deleting Third Party IT with ID: {}", id);
        ThirdPartyIT thirdPartyIT = thirdPartyITRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Third party IT not found"));
        log.info("Third Party IT found: {}", thirdPartyIT.getName());

        List<Client> clients = clientRepo.findByThirdPartyITsContaining(thirdPartyIT);
        log.info("Third Party IT associated with {} clients. Removing associations.", clients.size());
        for (Client client : clients) {
            client.getThirdPartyITs().remove(thirdPartyIT);
            clientRepo.save(client);
            log.info("Removed Third Party IT association from client with ID: {}", client.getId());
        }

        thirdPartyITRepo.delete(thirdPartyIT);
        log.info("Third Party IT with ID: {} deleted successfully", id);

        return new ResponseDTO("Third party IT deleted successfully");
    }

    @Transactional
    public ResponseDTO updateThirdPartyIT(Integer thirdPartyId, ThirdPartyITDTO thirdPartyITDTO) {
        log.info("Updating Third Party IT with ID: {}", thirdPartyId);
        Optional<ThirdPartyIT> thirdPartyITOpt = thirdPartyITRepo.findById(thirdPartyId);
        if (thirdPartyITOpt.isEmpty()) {
            log.error("Third Party IT with ID: {} not found", thirdPartyId);
            throw new EntityNotFoundException("Third Party IT with id " + thirdPartyId + " not found");
        }
        ThirdPartyIT thirdPartyIT = thirdPartyITOpt.get();

        updateName(thirdPartyIT, thirdPartyITDTO);
        updateEmail(thirdPartyIT, thirdPartyITDTO);
        updatePhone(thirdPartyIT, thirdPartyITDTO);
        thirdPartyITRepo.save(thirdPartyIT);
        log.info("Third Party IT with ID: {} updated successfully", thirdPartyId);
        return new ResponseDTO("Third party IT updated successfully");
    }

    public void updateName(ThirdPartyIT thirdPartyIT, ThirdPartyITDTO thirdPartyITDTO) {
        if (thirdPartyITDTO.name() != null) {
            thirdPartyIT.setName(thirdPartyITDTO.name());
        }
    }

    public void updateEmail(ThirdPartyIT thirdPartyIT, ThirdPartyITDTO thirdPartyITDTO) {
        if (thirdPartyITDTO.email() != null) {
            thirdPartyIT.setEmail(thirdPartyITDTO.email());
        }
    }

    public void updatePhone(ThirdPartyIT thirdPartyIT, ThirdPartyITDTO thirdPartyITDTO) {
        if (thirdPartyITDTO.phone() != null) {
            thirdPartyIT.setPhone(thirdPartyITDTO.phone());
        }
    }

    public List<ThirdPartyITDTO> getAllThirdParties() {
        log.info("Fetching all Third Party IT entries");
        List<ThirdPartyITDTO> thirdPartyITList = thirdPartyITMapper.toDtoList(thirdPartyITRepo.findAll());
        log.info("Found {} Third Party IT entries", thirdPartyITList.size());
        return thirdPartyITList;
    }

    public ThirdPartyITDTO getThirdPartyITById(Integer thirdPartyId) {
        log.info("Fetching Third Party IT with ID: {}", thirdPartyId);
        Optional<ThirdPartyIT> thirdPartyITOpt = thirdPartyITRepo.findById(thirdPartyId);
        if (thirdPartyITOpt.isEmpty()) {
            log.error("Third Party IT with ID: {} not found", thirdPartyId);
            throw new EntityNotFoundException("Third Party IT with id " + thirdPartyId + " not found");
        }
        ThirdPartyIT thirdPartyIT = thirdPartyITOpt.get();
        log.info("Successfully fetched Third Party IT with ID: {}", thirdPartyId);
        return thirdPartyITMapper.toDto(thirdPartyIT);
    }

    public Set<ThirdPartyIT> thirdPartyITIdsToThirdPartyITsSet(List<Integer> thirdPartyITIds) {
        log.info("Converting Third Party IT IDs to entity set: {}", thirdPartyITIds);
        Set<ThirdPartyIT> thirdPartyITs = new HashSet<>();
        for (Integer thirdPartyITId : thirdPartyITIds) {
            ThirdPartyIT thirdPartyIT = thirdPartyITRepo.findById(thirdPartyITId)
                    .orElseThrow(() -> {
                        log.error("Invalid Third Party IT ID: {}", thirdPartyITId);
                        return new IllegalArgumentException("Invalid third party IT ID: " + thirdPartyITId);
                    });
            thirdPartyITs.add(thirdPartyIT);
        }
        log.info("Converted {} Third Party IT IDs to entities", thirdPartyITs.size());
        return thirdPartyITs;
    }
}
