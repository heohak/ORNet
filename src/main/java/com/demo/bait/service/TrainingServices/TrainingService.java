package com.demo.bait.service.TrainingServices;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TrainingDTO;
import com.demo.bait.entity.BaitWorker;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.Training;
import com.demo.bait.mapper.TrainingMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.LocationRepo;
import com.demo.bait.repository.TrainingRepo;
import com.demo.bait.service.BaitWorkerServices.BaitWorkerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class TrainingService {

    private TrainingRepo trainingRepo;
    private TrainingMapper trainingMapper;
    private ClientRepo clientRepo;
    private LocationRepo locationRepo;
    private BaitWorkerService baitWorkerService;

    @Transactional
    public ResponseDTO addTraining(TrainingDTO trainingDTO) {
        log.info("Adding a new training: {}", trainingDTO);
        try {
            Training training = new Training();

            updateClient(training, trainingDTO);
            updateLocation(training, trainingDTO);
            updateTrainers(training, trainingDTO);

            training.setName(trainingDTO.name());
            training.setDescription(trainingDTO.description());
            training.setTrainingDate(trainingDTO.trainingDate());
            training.setTrainingType(trainingDTO.trainingType());

            trainingRepo.save(training);
            log.info("Successfully added training with ID: {}", training.getId());
            return new ResponseDTO(training.getId().toString());
        } catch (Exception e) {
            log.error("Error while adding training: {}", trainingDTO, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO updateTraining(Integer trainingId, TrainingDTO trainingDTO) {
        log.info("Updating training with ID: {}", trainingId);
        try {
            Optional<Training> trainingOpt = trainingRepo.findById(trainingId);
            if (trainingOpt.isEmpty()) {
                log.warn("Training with ID: {} not found", trainingId);
                throw new EntityNotFoundException("Training with ID: " +  trainingId + " not found");
            }
            Training training = trainingOpt.get();

            updateClient(training, trainingDTO);
            updateLocation(training, trainingDTO);
            updateName(training, trainingDTO);
            updateDescription(training, trainingDTO);
            updateTrainers(training, trainingDTO);
            updateTrainingDate(training, trainingDTO);
            updateTrainingType(training, trainingDTO);

            trainingRepo.save(training);
            log.info("Successfully updated training with ID: {}", trainingId);
            return new ResponseDTO("Training successfully updated");
        } catch (Exception e) {
            log.error("Error while updating training with ID: {}", trainingId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO deleteTraining(Integer trainingId) {
        log.info("Deleting training with ID: {}", trainingId);
        try {
            Optional<Training> trainingOpt = trainingRepo.findById(trainingId);
            if (trainingOpt.isEmpty()) {
                log.warn("Training with ID: {} not found", trainingId);
                throw new EntityNotFoundException("Training with ID: " +  trainingId + " not found");
            }
            Training training = trainingOpt.get();

            training.setClient(null);
            training.setLocation(null);
            training.getTrainers().clear();

            trainingRepo.delete(training);
            log.info("Successfully deleted training with ID: {}", trainingId);
            return new ResponseDTO("Training deleted successfully");
        } catch (Exception e) {
            log.error("Error while deleting training with ID: {}", trainingId, e);
            throw e;
        }
    }

    public void updateClient(Training training, TrainingDTO trainingDTO) {
        if (trainingDTO.clientId() != null) {
            Optional<Client> clientOpt = clientRepo.findById(trainingDTO.clientId());
            clientOpt.ifPresent(training::setClient);
        }
    }

    public void updateLocation(Training training, TrainingDTO trainingDTO) {
        if (trainingDTO.locationId() != null) {
            Optional<Location> locationOpt = locationRepo.findById(trainingDTO.locationId());
            locationOpt.ifPresent(training::setLocation);
        } else {
            training.setLocation(null);
        }
    }

    public void updateTrainers(Training training, TrainingDTO trainingDTO) {
        if (trainingDTO.trainersIds() != null) {
            Set<BaitWorker> trainers = baitWorkerService.baitWorkerIdsToBaitWorkersSet(trainingDTO.trainersIds());
            training.setTrainers(trainers);
        }
    }

    public void updateName(Training training, TrainingDTO trainingDTO) {
        if (trainingDTO.name() != null) {
            training.setName(trainingDTO.name());
        }
    }

    public void updateDescription(Training training, TrainingDTO trainingDTO) {
        if (trainingDTO.description() != null) {
            training.setDescription(trainingDTO.description());
        }
    }

    public void updateTrainingDate(Training training, TrainingDTO trainingDTO) {
        if (trainingDTO.trainingDate() != null) {
            training.setTrainingDate(trainingDTO.trainingDate());
        }
    }

    public void updateTrainingType(Training training, TrainingDTO trainingDTO) {
        if (trainingDTO.trainingType() != null) {
            training.setTrainingType(trainingDTO.trainingType());
        }
    }

    public List<TrainingDTO> getAllTrainings() {
        log.info("Fetching all trainings.");
        try {
            List<TrainingDTO> trainings = trainingMapper.toDtoList(trainingRepo.findAll());
            log.info("Fetched {} trainings.", trainings.size());
            return trainings;
        } catch (Exception e) {
            log.error("Error while fetching all trainings.", e);
            throw e;
        }
    }

    public TrainingDTO getTraining(Integer trainingId) {
        if (trainingId == null) {
            log.warn("Training id is null. Returning null.");
            return null;
        }

        log.info("Fetching training with ID: {}", trainingId);
        try {
            Optional<Training> trainingOpt = trainingRepo.findById(trainingId);
            if (trainingOpt.isEmpty()) {
                log.warn("Training with ID: {} not found", trainingId);
                throw new EntityNotFoundException("Training with ID: " +  trainingId + " not found");
            }
            Training training = trainingOpt.get();
            return trainingMapper.toDto(training);
        } catch (Exception e) {
            log.error("Error while fetching training with ID: {}", trainingId, e);
            throw e;
        }
    }

    public List<TrainingDTO> getClientTrainings(Integer clientId) {
        log.info("Fetching all client trainings with clientId {}.", clientId);

        if (clientId == null) {
            log.warn("Client ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        try {
            Optional<Client> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                log.error("Client with ID {} not found", clientId);
                throw new EntityNotFoundException("Client with ID " + clientId + " not found");
            }
            Client client = clientOpt.get();
            List<Training> trainings = trainingRepo.findAllByClient(client);
            log.info("Fetched {} trainings for client with ID: {}", trainings.size(),clientId);
            return trainingMapper.toDtoList(trainings);
        } catch (Exception e) {
            log.error("Error while fetching trainings for client with ID: {}", clientId, e);
            throw e;
        }
    }

    public LocalDate getLastTrainingDateForClient(Integer clientId) {
        log.info("Getting last training date for client with ID: {}", clientId);

        if (clientId == null) {
            log.warn("Client ID is null. Returning null.");
            return null;
        }

        try {
            Optional<Client> clientOpt = clientRepo.findById(clientId);
            if (clientOpt.isEmpty()) {
                log.error("Client with ID {} not found", clientId);
                throw new EntityNotFoundException("Client with ID " + clientId + " not found");
            }
            Client client = clientOpt.get();
            LocalDate today = LocalDate.now();
            return trainingRepo.findAllByClient(client).stream()
                    .map(Training::getTrainingDate)
                    .filter(date -> date != null && !date.isAfter(today))
                    .max(Comparator.naturalOrder())
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error while getting last training date for client with ID: {}", clientId, e);
            throw e;
        }
    }
}
