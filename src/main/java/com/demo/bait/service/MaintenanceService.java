package com.demo.bait.service;

import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.repository.FileUploadRepo;
import com.demo.bait.repository.MaintenanceRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class MaintenanceService {

    private MaintenanceRepo maintenanceRepo;
    private MaintenanceMapper maintenanceMapper;
    private FileUploadRepo fileUploadRepo;
    private FileUploadService fileUploadService;

    public ResponseDTO addMaintenance(MaintenanceDTO maintenanceDTO) {
        Maintenance maintenance = new Maintenance();
        maintenance.setMaintenanceName(maintenanceDTO.maintenanceName());
        maintenance.setMaintenanceDate(maintenanceDTO.maintenanceDate());
        maintenance.setComment(maintenanceDTO.comment());
        // set file
        if (maintenanceDTO.fileIds() != null) {
            Set<FileUpload> files = new HashSet<>();
            for (Integer fileId : maintenanceDTO.fileIds()) {
                FileUpload fileUpload = fileUploadRepo.findById(fileId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid file ID: " + fileId));
                files.add(fileUpload);
            }
            maintenance.setFiles(files);
        }
        maintenanceRepo.save(maintenance);
        return new ResponseDTO("Maintenance added successfully");
    }

    public List<MaintenanceDTO> getAllMaintenances() {
        return maintenanceMapper.toDtoList(maintenanceRepo.findAll());
    }

    @Transactional
    public ResponseDTO uploadFilesToMaintenance(Integer maintenanceId, List<MultipartFile> files) throws IOException {
        Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);
        if (maintenanceOpt.isEmpty()) {
            throw new EntityNotFoundException("Maintenance with id " + maintenanceId + " not found");
        }
        Maintenance maintenance = maintenanceOpt.get();
        Set<FileUpload> uploadedFiles = fileUploadService.uploadFiles(files);
        maintenance.getFiles().addAll(uploadedFiles);
        maintenanceRepo.save(maintenance);
        return new ResponseDTO("Files uploaded successfully to maintenance");
    }
}
