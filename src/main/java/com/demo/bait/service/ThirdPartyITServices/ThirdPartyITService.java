package com.demo.bait.service.ThirdPartyITServices;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.entity.ThirdPartyIT;
import com.demo.bait.mapper.ThirdPartyITMapper;
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

    @Transactional
    public ThirdPartyITDTO addThirdPartyIT(ThirdPartyITDTO thirdPartyITDTO) {
        ThirdPartyIT thirdPartyIT = new ThirdPartyIT();
        thirdPartyIT.setName(thirdPartyITDTO.name());
        thirdPartyIT.setEmail(thirdPartyITDTO.email());
        thirdPartyIT.setPhone(thirdPartyITDTO.phone());
        ThirdPartyIT savedThirdParty = thirdPartyITRepo.save(thirdPartyIT);
        return thirdPartyITMapper.toDto(savedThirdParty);
    }

    @Transactional
    public ResponseDTO deleteThirdPartyIT(Integer id) {
        thirdPartyITRepo.deleteById(id);
        return new ResponseDTO("Third party IT deleted successfully");
    }

    @Transactional
    public ResponseDTO updateThirdPartyIT(Integer thirdPartyId, ThirdPartyITDTO thirdPartyITDTO) {
        Optional<ThirdPartyIT> thirdPartyITOpt = thirdPartyITRepo.findById(thirdPartyId);
        if (thirdPartyITOpt.isEmpty()) {
            throw new EntityNotFoundException("Third Party IT with id " + thirdPartyId + " not found");
        }
        ThirdPartyIT thirdPartyIT = thirdPartyITOpt.get();

        updateName(thirdPartyIT, thirdPartyITDTO);
        updateEmail(thirdPartyIT, thirdPartyITDTO);
        updatePhone(thirdPartyIT, thirdPartyITDTO);
        thirdPartyITRepo.save(thirdPartyIT);
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
        return thirdPartyITMapper.toDtoList(thirdPartyITRepo.findAll());
    }

    public Set<ThirdPartyIT> thirdPartyITIdsToThirdPartyITsSet(List<Integer> thirdPartyITIds) {
        Set<ThirdPartyIT> thirdPartyITs = new HashSet<>();
        for (Integer thirdPartyITId : thirdPartyITIds) {
            ThirdPartyIT thirdPartyIT = thirdPartyITRepo.findById(thirdPartyITId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid third party IT ID: " + thirdPartyITId));
            thirdPartyITs.add(thirdPartyIT);
        }
        return thirdPartyITs;
    }
}
