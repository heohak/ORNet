package com.demo.bait.service;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.entity.ThirdPartyIT;
import com.demo.bait.mapper.ThirdPartyITMapper;
import com.demo.bait.repository.ThirdPartyITRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ThirdPartyITService {

    private ThirdPartyITRepo thirdPartyITRepo;
    private ThirdPartyITMapper thirdPartyITMapper;

    public ResponseDTO addThirdPartyIT(ThirdPartyITDTO thirdPartyITDTO) {
        ThirdPartyIT thirdPartyIT = new ThirdPartyIT();
        thirdPartyIT.setName(thirdPartyITDTO.name());
        thirdPartyIT.setEmail(thirdPartyITDTO.email());
        thirdPartyIT.setPhone(thirdPartyITDTO.phone());
        thirdPartyITRepo.save(thirdPartyIT);
        return new ResponseDTO("Third party IT added successfully");
    }

    public List<ThirdPartyITDTO> getAllThirdParties() {
        return thirdPartyITMapper.toDtoList(thirdPartyITRepo.findAll());
    }

    public ResponseDTO deleteThirdPartyIT(Integer id) {
        thirdPartyITRepo.deleteById(id);
        return new ResponseDTO("Third party IT deleted successfully");
    }
}
