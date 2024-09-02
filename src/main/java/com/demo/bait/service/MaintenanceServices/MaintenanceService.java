package com.demo.bait.service.MaintenanceServices;

import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.mapper.FileUploadMapper;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.repository.FileUploadRepo;
import com.demo.bait.repository.MaintenanceRepo;
import com.demo.bait.service.FileUploadServices.FileUploadService;
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
    private FileUploadService fileUploadService;

    @Transactional
    public ResponseDTO addMaintenance(MaintenanceDTO maintenanceDTO) {
        Maintenance maintenance = new Maintenance();
        maintenance.setMaintenanceName(maintenanceDTO.maintenanceName());
        maintenance.setMaintenanceDate(maintenanceDTO.maintenanceDate());
        maintenance.setComment(maintenanceDTO.comment());

        if (maintenanceDTO.fileIds() != null) {
            Set<FileUpload> files = fileUploadService.fileIdsToFilesSet(maintenanceDTO.fileIds());
            maintenance.setFiles(files);
        }
        maintenanceRepo.save(maintenance);
        return new ResponseDTO(maintenance.getId().toString());
    }

    public List<MaintenanceDTO> getAllMaintenances() {
        return maintenanceMapper.toDtoList(maintenanceRepo.findAll());
    }

    public Set<Maintenance> maintenanceIdsToMaintenancesSet(List<Integer> maintenanceIds) {
        Set<Maintenance> maintenances = new HashSet<>();
        for (Integer id : maintenanceIds) {
            Maintenance maintenance = maintenanceRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance ID: " + id));
            maintenances.add(maintenance);
        }
        return maintenances;
    }

    public MaintenanceDTO getMaintenanceById(Integer id) {
        Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(id);

        if (maintenanceOpt.isEmpty()) {
            throw new EntityNotFoundException("Maintenance with " + id + " not found");
        }

        Maintenance maintenance = maintenanceOpt.get();
        return maintenanceMapper.toDto(maintenance);
    }
}
