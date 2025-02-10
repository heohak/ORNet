package com.demo.bait.service.MaintenanceCommentService;

import com.demo.bait.dto.MaintenanceCommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.*;
import com.demo.bait.mapper.MaintenanceCommentMapper;
import com.demo.bait.repository.*;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class MaintenanceCommentService {

    private MaintenanceCommentRepo maintenanceCommentRepo;
    private MaintenanceCommentMapper maintenanceCommentMapper;
    private MaintenanceRepo maintenanceRepo;
    private FileUploadService fileUploadService;
    private DeviceRepo deviceRepo;
    private LinkedDeviceRepo linkedDeviceRepo;
    private SoftwareRepo softwareRepo;

    @Transactional
    public ResponseDTO addMaintenanceComment(MaintenanceCommentDTO maintenanceCommentDTO) {
        log.info("Adding a new maintenance comment record");
        try {
            MaintenanceComment maintenanceComment = new MaintenanceComment();

            if (maintenanceCommentDTO.maintenanceId() != null) {
                Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceCommentDTO.maintenanceId());
                maintenanceOpt.ifPresent(maintenanceComment::setMaintenance);
            }

            if (maintenanceCommentDTO.deviceId() != null) {
                Optional<Device> deviceOpt = deviceRepo.findById(maintenanceCommentDTO.deviceId());
                deviceOpt.ifPresent(maintenanceComment::setDevice);
            }

            if (maintenanceCommentDTO.linkedDeviceId() != null) {
                Optional<LinkedDevice> linkedDeviceOpt = linkedDeviceRepo.findById(maintenanceCommentDTO.linkedDeviceId());
                linkedDeviceOpt.ifPresent(maintenanceComment::setLinkedDevice);
            }

            if (maintenanceCommentDTO.softwareId() != null) {
                Optional<Software> softwareOpt = softwareRepo.findById(maintenanceCommentDTO.softwareId());
                softwareOpt.ifPresent(maintenanceComment::setSoftware);
            }

            maintenanceComment.setComment(maintenanceCommentDTO.comment());
            maintenanceComment.setMaintenanceStatus(maintenanceCommentDTO.maintenanceStatus());

            if (maintenanceCommentDTO.fileIds() != null) {
                Set<FileUpload> files = fileUploadService.fileIdsToFilesSet(maintenanceCommentDTO.fileIds());
                maintenanceComment.setFiles(files);
            }

            maintenanceCommentRepo.save(maintenanceComment);
            log.info("Maintenance comment record added successfully with ID: {}", maintenanceComment.getId());
            return new ResponseDTO(maintenanceComment.getId().toString());
        } catch (Exception e) {
            log.error("Error while adding maintenance comment record", e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO updateMaintenanceComment(Integer maintenanceCommentId,
                                                MaintenanceCommentDTO maintenanceCommentDTO) {
        log.info("Updating maintenance comment record with ID: {}", maintenanceCommentId);
        try {
            Optional<MaintenanceComment> maintenanceCommentOpt = maintenanceCommentRepo.findById(maintenanceCommentId);
            if (maintenanceCommentOpt.isEmpty()) {
                log.warn("Maintenance comment record with ID {} not found", maintenanceCommentId);
                throw new EntityNotFoundException("Maintenance comment record with ID " + maintenanceCommentId + " not found");
            }
            MaintenanceComment maintenanceComment = maintenanceCommentOpt.get();

            updateMaintenance(maintenanceComment, maintenanceCommentDTO);
            updateDevice(maintenanceComment, maintenanceCommentDTO);
            updateLinkedDevice(maintenanceComment, maintenanceCommentDTO);
            updateSoftware(maintenanceComment, maintenanceCommentDTO);
            updateComment(maintenanceComment, maintenanceCommentDTO);
            updateMaintenanceStatus(maintenanceComment, maintenanceCommentDTO);
            updateFiles(maintenanceComment, maintenanceCommentDTO);

            maintenanceCommentRepo.save(maintenanceComment);
            log.info("Maintenance comment record updated successfully with ID: {}", maintenanceCommentId);
            return new ResponseDTO("Maintenance comment updated successfully");
        } catch (Exception e) {
            log.error("Error while updating maintenance comment record with ID: {}", maintenanceCommentId, e);
            throw e;
        }
    }

    public void updateMaintenance(MaintenanceComment maintenanceComment, MaintenanceCommentDTO maintenanceCommentDTO) {
        if (maintenanceCommentDTO.maintenanceId() != null) {
            Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceCommentDTO.maintenanceId());
            maintenanceOpt.ifPresent(maintenanceComment::setMaintenance);
        }
    }

    public void updateDevice(MaintenanceComment maintenanceComment, MaintenanceCommentDTO maintenanceCommentDTO) {
        if (maintenanceCommentDTO.deviceId() != null) {
            Optional<Device> deviceOpt = deviceRepo.findById(maintenanceCommentDTO.deviceId());
            deviceOpt.ifPresent(maintenanceComment::setDevice);
        }
    }

    public void updateLinkedDevice(MaintenanceComment maintenanceComment, MaintenanceCommentDTO maintenanceCommentDTO) {
        if (maintenanceCommentDTO.linkedDeviceId() != null) {
            Optional<LinkedDevice> linkedDeviceOpt = linkedDeviceRepo.findById(maintenanceCommentDTO.linkedDeviceId());
            linkedDeviceOpt.ifPresent(maintenanceComment::setLinkedDevice);
        }
    }

    public void updateSoftware(MaintenanceComment maintenanceComment, MaintenanceCommentDTO maintenanceCommentDTO) {
        if (maintenanceCommentDTO.softwareId() != null) {
            Optional<Software> softwareOpt = softwareRepo.findById(maintenanceCommentDTO.softwareId());
            softwareOpt.ifPresent(maintenanceComment::setSoftware);
        }
    }

    public  void updateComment(MaintenanceComment maintenanceComment, MaintenanceCommentDTO maintenanceCommentDTO) {
        if (maintenanceCommentDTO.comment() != null) {
            maintenanceComment.setComment(maintenanceCommentDTO.comment());
        }
    }

    public  void updateMaintenanceStatus(MaintenanceComment maintenanceComment, MaintenanceCommentDTO maintenanceCommentDTO) {
        if (maintenanceCommentDTO.maintenanceStatus() != null) {
            maintenanceComment.setMaintenanceStatus(maintenanceCommentDTO.maintenanceStatus());
        }
    }
    public  void updateFiles(MaintenanceComment maintenanceComment, MaintenanceCommentDTO maintenanceCommentDTO) {
        if (maintenanceCommentDTO.fileIds() != null) {
            log.debug("Updating attached files for maintenance comment record");
            Set<FileUpload> files = fileUploadService.fileIdsToFilesSet(maintenanceCommentDTO.fileIds());
            maintenanceComment.setFiles(files);
        }
    }

    public MaintenanceCommentDTO getMaintenanceCommentById(Integer id) {
        if (id == null) {
            log.warn("Maintenance comment ID is null. Returning null.");
            return null;
        }

        log.info("Fetching maintenance comment record with ID: {}", id);
        try {
            Optional<MaintenanceComment> maintenanceCommentOpt = maintenanceCommentRepo.findById(id);
            if (maintenanceCommentOpt.isEmpty()) {
                log.warn("Maintenance comment record with ID {} not found", id);
                throw new EntityNotFoundException("Maintenance comment record with ID " + id + " not found");
            }
            MaintenanceComment maintenanceComment = maintenanceCommentOpt.get();
            log.info("Maintenance comment record with ID {} fetched successfully", id);
            return maintenanceCommentMapper.toDto(maintenanceComment);
        } catch (Exception e) {
            log.error("Error while fetching maintenance comment record with ID: {}", id, e);
            throw e;
        }
    }

    public List<MaintenanceCommentDTO> getMaintenanceCommentsForMaintenance(Integer maintenanceId) {
        if (maintenanceId == null) {
            log.warn("Maintenance ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        try {
            Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);
            if (maintenanceOpt.isEmpty()) {
                log.warn("Maintenance record with ID {} not found", maintenanceId);
                throw new EntityNotFoundException("Maintenance with " + maintenanceId + " not found");
            }
            Maintenance maintenance = maintenanceOpt.get();

            List<MaintenanceComment> maintenanceComments = maintenanceCommentRepo.findAllByMaintenance(maintenance);
            log.info("Fetched {} maintenance comment records for maintenance with ID: {}", maintenanceComments.size(), maintenanceId);
            return maintenanceCommentMapper.toDtoList(maintenanceComments);
        } catch (Exception e) {
            log.error("Error fetching maintenance comments for maintenance ID: " + maintenanceId, e);
            throw e;
        }
    }
}
