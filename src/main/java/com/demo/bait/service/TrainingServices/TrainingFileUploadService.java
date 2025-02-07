package com.demo.bait.service.TrainingServices;

import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.Training;
import com.demo.bait.mapper.FileUploadMapper;
import com.demo.bait.repository.TrainingRepo;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class TrainingFileUploadService {

    private final TrainingRepo trainingRepo;
    private final FileUploadService fileUploadService;
    private final FileUploadMapper fileUploadMapper;


    @Transactional
    public ResponseDTO uploadFilesToTraining(Integer trainingId, List<MultipartFile> files) throws IOException {
        log.info("Starting file upload to training with ID: {}", trainingId);

        Optional<Training> trainingOpt = trainingRepo.findById(trainingId);
        if (trainingOpt.isEmpty()) {
            log.error("Training with ID: {} not found", trainingId);
            throw new EntityNotFoundException("Training with id " + trainingId + " not found");
        }

        Training training = trainingOpt.get();
        log.debug("Uploading {} files to training with ID: {}", files.size(), trainingId);
        Set<FileUpload> uploadedFiles = fileUploadService.uploadFiles(files);

        training.getFiles().addAll(uploadedFiles);
        trainingRepo.save(training);

        log.info("Files uploaded successfully to training with ID: {}. Total files uploaded: {}", trainingId, uploadedFiles.size());
        return new ResponseDTO("Files uploaded successfully to training");
    }

    public List<FileUploadDTO> getTrainingFiles(Integer trainingId) {
        if (trainingId == null) {
            log.warn("Training ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        log.info("Fetching files for training with ID: {}", trainingId);

        Optional<Training> trainingOpt = trainingRepo.findById(trainingId);
        if (trainingOpt.isEmpty()) {
            log.error("Training with ID: {} not found", trainingId);
            throw new EntityNotFoundException("Training with id " + trainingId + " not found");
        }

        Training training = trainingOpt.get();
        List<FileUploadDTO> files = fileUploadMapper.toDtoList(training.getFiles().stream().toList());
        log.info("Found {} files for training with ID: {}", files.size(), trainingId);
        return files;
    }
}
