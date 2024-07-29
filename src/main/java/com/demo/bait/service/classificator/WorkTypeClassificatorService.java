package com.demo.bait.service.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.WorkTypeClassificatorDTO;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.classificator.WorkTypeClassificatorMapper;
import com.demo.bait.repository.classificator.WorkTypeClassificatorRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class WorkTypeClassificatorService {

    private WorkTypeClassificatorRepo workTypeClassificatorRepo;
    private WorkTypeClassificatorMapper workTypeClassificatorMapper;

    public ResponseDTO addWorkTypeClassificator(WorkTypeClassificatorDTO workTypeClassificatorDTO) {
        WorkTypeClassificator workTypeClassificator = new WorkTypeClassificator();
        workTypeClassificator.setWorkType(workTypeClassificatorDTO.workType());
        workTypeClassificatorRepo.save(workTypeClassificator);
        return new ResponseDTO("Work Type classificator added successfully");
    }

    public List<WorkTypeClassificatorDTO> getAllWorkTypes() {
        return workTypeClassificatorMapper.toDtoList(workTypeClassificatorRepo.findAll());
    }
}
