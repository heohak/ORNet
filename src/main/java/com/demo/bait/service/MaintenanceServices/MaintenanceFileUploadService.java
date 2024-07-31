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

    public List<FileUploadDTO> getMaintenanceFiles(Integer maintenanceId) {
        Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);
        if (maintenanceOpt.isEmpty()) {
            throw new EntityNotFoundException("Maintenance with id " + maintenanceId + " not found");
        }
        Maintenance maintenance = maintenanceOpt.get();
        return fileUploadMapper.toDtoList(maintenance.getFiles().stream().toList());
    }
}
