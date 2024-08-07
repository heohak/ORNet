package com.demo.bait.service.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.WorkTypeClassificatorDTO;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.classificator.WorkTypeClassificatorMapper;
import com.demo.bait.repository.classificator.WorkTypeClassificatorRepo;
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
public class WorkTypeClassificatorService {

    private WorkTypeClassificatorRepo workTypeClassificatorRepo;
    private WorkTypeClassificatorMapper workTypeClassificatorMapper;

    @Transactional
    public ResponseDTO addWorkTypeClassificator(WorkTypeClassificatorDTO workTypeClassificatorDTO) {
        WorkTypeClassificator workTypeClassificator = new WorkTypeClassificator();
        workTypeClassificator.setWorkType(workTypeClassificatorDTO.workType());
        workTypeClassificatorRepo.save(workTypeClassificator);
        return new ResponseDTO("Work Type classificator added successfully");
    }

    @Transactional
    public ResponseDTO updateWorkTypeClassificator(Integer workTypeId,
                                                   WorkTypeClassificatorDTO workTypeClassificatorDTO) {
        Optional<WorkTypeClassificator> workTypeOpt = workTypeClassificatorRepo.findById(workTypeId);
        if(workTypeOpt.isEmpty()) {
            throw new EntityNotFoundException("Work Type classificator witn id " + workTypeId + " not found");
        }
        WorkTypeClassificator workType = workTypeOpt.get();

        if (workTypeClassificatorDTO.workType() != null) {
            workType.setWorkType(workTypeClassificatorDTO.workType());
        }
        workTypeClassificatorRepo.save(workType);
        return new ResponseDTO("Work type classificator updated successfully");
    }

    public List<WorkTypeClassificatorDTO> getAllWorkTypes() {
        return workTypeClassificatorMapper.toDtoList(workTypeClassificatorRepo.findAll());
    }

    public Set<WorkTypeClassificator> workTypeIdsToWorkTypesSet(List<Integer> workTypeIds) {
        Set<WorkTypeClassificator> workTypes = new HashSet<>();
        for (Integer id : workTypeIds) {
            WorkTypeClassificator workType = workTypeClassificatorRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid work type classificator ID: " + id));
            workTypes.add(workType);
        }
        return workTypes;
    }
}
