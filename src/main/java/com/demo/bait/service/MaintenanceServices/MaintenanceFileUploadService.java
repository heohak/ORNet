package com.demo.bait.service.MaintenanceServices;

import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.mapper.FileUploadMapper;
import com.demo.bait.repository.MaintenanceRepo;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class MaintenanceFileUploadService {

    private MaintenanceRepo maintenanceRepo;
    private FileUploadService fileUploadService;
    private FileUploadMapper fileUploadMapper;

    @Transactional
    public ResponseDTO uploadFilesToMaintenance(Integer maintenanceId, List<MultipartFile> files) throws IOException {
        log.info("Uploading files to maintenance with ID: {}", maintenanceId);
        try {
            Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);
            if (maintenanceOpt.isEmpty()) {
                log.warn("Maintenance with ID {} not found", maintenanceId);
                throw new EntityNotFoundException("Maintenance with id " + maintenanceId + " not found");
            }

            Maintenance maintenance = maintenanceOpt.get();
            log.debug("Found maintenance with ID: {}", maintenanceId);

            Set<FileUpload> uploadedFiles = fileUploadService.uploadFiles(files);
            maintenance.getFiles().addAll(uploadedFiles);
            maintenanceRepo.save(maintenance);

            log.info("Successfully uploaded {} files to maintenance with ID: {}", uploadedFiles.size(), maintenanceId);
            return new ResponseDTO("Files uploaded successfully to maintenance");
        } catch (Exception e) {
            log.error("Error while uploading files to maintenance with ID: {}", maintenanceId, e);
            throw e;
        }
    }

    public List<FileUploadDTO> getMaintenanceFiles(Integer maintenanceId) {
        log.info("Fetching files for maintenance with ID: {}", maintenanceId);
        try {
            Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);
            if (maintenanceOpt.isEmpty()) {
                log.warn("Maintenance with ID {} not found", maintenanceId);
                throw new EntityNotFoundException("Maintenance with id " + maintenanceId + " not found");
            }

            Maintenance maintenance = maintenanceOpt.get();
            List<FileUploadDTO> files = fileUploadMapper.toDtoList(maintenance.getFiles().stream().toList());
            log.info("Fetched {} files for maintenance with ID: {}", files.size(), maintenanceId);
            return files;
        } catch (Exception e) {
            log.error("Error while fetching files for maintenance with ID: {}", maintenanceId, e);
            throw e;
        }
    }
}
