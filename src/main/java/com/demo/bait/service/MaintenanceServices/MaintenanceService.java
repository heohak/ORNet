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
        log.info("Adding a new maintenance record with name: '{}'", maintenanceDTO.maintenanceName());
        try {
            Maintenance maintenance = new Maintenance();
            maintenance.setMaintenanceName(maintenanceDTO.maintenanceName());
            maintenance.setMaintenanceDate(maintenanceDTO.maintenanceDate());
            maintenance.setComment(maintenanceDTO.comment());

            if (maintenanceDTO.fileIds() != null) {
                log.debug("Attaching {} files to maintenance record", maintenanceDTO.fileIds().size());
                Set<FileUpload> files = fileUploadService.fileIdsToFilesSet(maintenanceDTO.fileIds());
                maintenance.setFiles(files);
            }

            maintenanceRepo.save(maintenance);
            log.info("Maintenance record added successfully with ID: {}", maintenance.getId());
            return new ResponseDTO(maintenance.getId().toString());
        } catch (Exception e) {
            log.error("Error while adding maintenance record", e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO updateMaintenance(Integer maintenanceId, MaintenanceDTO maintenanceDTO) {
        log.info("Updating maintenance record with ID: {}", maintenanceId);
        try {
            Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);
            if (maintenanceOpt.isEmpty()) {
                log.warn("Maintenance record with ID {} not found", maintenanceId);
                throw new EntityNotFoundException("Maintenance with " + maintenanceId + " not found");
            }

            Maintenance maintenance = maintenanceOpt.get();
            updateMaintenanceName(maintenance, maintenanceDTO);
            updateMaintenanceDate(maintenance, maintenanceDTO);
            updateMaintenanceComment(maintenance, maintenanceDTO);
            maintenanceRepo.save(maintenance);

            log.info("Maintenance record updated successfully with ID: {}", maintenanceId);
            return new ResponseDTO("Maintenance updated successfully");
        } catch (Exception e) {
            log.error("Error while updating maintenance record with ID: {}", maintenanceId, e);
            throw e;
        }
    }

    public void updateMaintenanceName(Maintenance maintenance, MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.maintenanceName() != null) {
            maintenance.setMaintenanceName(maintenanceDTO.maintenanceName());
        }
    }

    public void updateMaintenanceDate(Maintenance maintenance, MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.maintenanceDate() != null) {
            maintenance.setMaintenanceDate(maintenanceDTO.maintenanceDate());
        }
    }

    public void updateMaintenanceComment(Maintenance maintenance, MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.comment() != null) {
            maintenance.setComment(maintenanceDTO.comment());
        }
    }

    public List<MaintenanceDTO> getAllMaintenances() {
        log.info("Fetching all maintenance records");
        try {
            List<MaintenanceDTO> maintenances = maintenanceMapper.toDtoList(maintenanceRepo.findAll());
            log.info("Fetched {} maintenance records", maintenances.size());
            return maintenances;
        } catch (Exception e) {
            log.error("Error while fetching all maintenance records", e);
            throw e;
        }
    }

    public Set<Maintenance> maintenanceIdsToMaintenancesSet(List<Integer> maintenanceIds) {
        log.info("Fetching maintenance records for IDs: {}", maintenanceIds);
        try {
            Set<Maintenance> maintenances = new HashSet<>();
            for (Integer id : maintenanceIds) {
                Maintenance maintenance = maintenanceRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance ID: " + id));
                maintenances.add(maintenance);
            }
            log.info("Fetched {} maintenance records for provided IDs", maintenances.size());
            return maintenances;
        } catch (Exception e) {
            log.error("Error while fetching maintenance records for IDs: {}", maintenanceIds, e);
            throw e;
        }
    }

    public MaintenanceDTO getMaintenanceById(Integer id) {
        log.info("Fetching maintenance record with ID: {}", id);
        try {
            Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(id);
            if (maintenanceOpt.isEmpty()) {
                log.warn("Maintenance record with ID {} not found", id);
                throw new EntityNotFoundException("Maintenance with " + id + " not found");
            }

            Maintenance maintenance = maintenanceOpt.get();
            log.info("Maintenance record with ID {} fetched successfully", id);
            return maintenanceMapper.toDto(maintenance);
        } catch (Exception e) {
            log.error("Error while fetching maintenance record with ID: {}", id, e);
            throw e;
        }
    }
}
