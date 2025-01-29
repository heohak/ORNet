package com.demo.bait.service.TrainingServices;

import com.demo.bait.dto.TrainingDTO;
import com.demo.bait.entity.Training;
import com.demo.bait.enums.TrainingType;
import com.demo.bait.mapper.TrainingMapper;
import com.demo.bait.repository.TrainingRepo;
import com.demo.bait.specification.TrainingSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TrainingSpecificationService {

    private TrainingRepo trainingRepo;
    private TrainingMapper trainingMapper;

    public List<TrainingDTO> searchAndFilterTrainings(String searchTerm, Integer clientId, Integer locationId,
                                                      LocalDate trainingDate, TrainingType trainingType) {
        log.info("Searching and filtering trainings");

        Specification<Training> combinedSpec = Specification.where(null);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            log.debug("Adding search term specification: {}", searchTerm);
            combinedSpec = combinedSpec.and(new TrainingSpecification(searchTerm));
        }

        if (clientId != null) {
            log.debug("Adding client ID specification: {}", clientId);
            combinedSpec = combinedSpec.and(TrainingSpecification.hasClientId(clientId));
        }

        if (locationId != null) {
            log.debug("Adding location ID specification: {}", locationId);
            combinedSpec = combinedSpec.and(TrainingSpecification.hasLocationId(locationId));
        }

        if (trainingDate != null) {
            log.debug("Adding training date specification: {}", trainingDate);
            combinedSpec = combinedSpec.and(TrainingSpecification.hasTrainingDate(trainingDate));
        }

        if (trainingType != null) {
            log.debug("Adding training type specification: {}", trainingType);
            combinedSpec = combinedSpec.and(TrainingSpecification.hasTrainingType(trainingType));
        }

        List<Training> filteredTrainings = trainingRepo.findAll(combinedSpec,
                Sort.by(Sort.Direction.DESC, "trainingDate"));

        log.info("Found {} trainings matching criteria", filteredTrainings.size());

        return trainingMapper.toDtoList(filteredTrainings);
    }
}
