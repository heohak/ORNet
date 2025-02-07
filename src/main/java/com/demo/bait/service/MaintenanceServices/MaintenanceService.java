package com.demo.bait.service.MaintenanceServices;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.*;
import com.demo.bait.mapper.*;
import com.demo.bait.repository.BaitWorkerRepo;
import com.demo.bait.repository.FileUploadRepo;
import com.demo.bait.repository.LocationRepo;
import com.demo.bait.repository.MaintenanceRepo;
import com.demo.bait.service.DeviceServices.DeviceHelperService;
import com.demo.bait.service.DeviceServices.DeviceService;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import com.demo.bait.service.LinkedDeviceServices.LinkedDeviceService;
import com.demo.bait.service.SoftwareServices.SoftwareService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MaintenanceService {

    private MaintenanceRepo maintenanceRepo;
    private MaintenanceMapper maintenanceMapper;
    private FileUploadService fileUploadService;
    private LocationRepo locationRepo;
    private BaitWorkerRepo baitWorkerRepo;
    private DeviceHelperService deviceHelperService;
    private LinkedDeviceService linkedDeviceService;
    private SoftwareService softwareService;
    private DeviceMapper deviceMapper;
    private LinkedDeviceMapper linkedDeviceMapper;
    private SoftwareMapper softwareMapper;

    @Transactional
    public ResponseDTO addMaintenance(MaintenanceDTO maintenanceDTO) {
        log.info("Adding a new maintenance record with name: '{}'", maintenanceDTO.maintenanceName());
        try {
            Maintenance maintenance = new Maintenance();
            maintenance.setMaintenanceName(maintenanceDTO.maintenanceName());
            maintenance.setMaintenanceDate(maintenanceDTO.maintenanceDate());
            maintenance.setLastDate(maintenanceDTO.lastDate());
            maintenance.setComment(maintenanceDTO.comment());

            if (maintenanceDTO.fileIds() != null) {
                log.debug("Attaching {} files to maintenance record", maintenanceDTO.fileIds().size());
                Set<FileUpload> files = fileUploadService.fileIdsToFilesSet(maintenanceDTO.fileIds());
                maintenance.setFiles(files);
            }

            if (maintenanceDTO.locationId() != null) {
                Optional<Location> locationOpt = locationRepo.findById(maintenanceDTO.locationId());
                locationOpt.ifPresent(maintenance::setLocation);
            }

            maintenance.setMaintenanceStatus(maintenanceDTO.maintenanceStatus());
            maintenance.setTimeSpent(Duration.ZERO);

            if (maintenanceDTO.baitWorkerId() != null) {
                Optional<BaitWorker> baitWorkerOpt = baitWorkerRepo.findById(maintenanceDTO.baitWorkerId());
                baitWorkerOpt.ifPresent(maintenance::setBaitWorker);
            }

            if (maintenanceDTO.deviceIds() != null) {
                log.debug("Attaching {} devices to maintenance record", maintenanceDTO.deviceIds().size());
                Set<Device> devices = deviceHelperService.deviceIdsToDevicesSet(maintenanceDTO.deviceIds());
                maintenance.setDevices(devices);
            }

            if (maintenanceDTO.linkedDeviceIds() != null) {
                log.debug("Attaching {} linked devices to maintenance record", maintenanceDTO.linkedDeviceIds().size());
                Set<LinkedDevice> linkedDevices = linkedDeviceService.linkedDeviceIdsToLinkedDeviceSet(
                        maintenanceDTO.linkedDeviceIds());
                maintenance.setLinkedDevices(linkedDevices);
            }

            if (maintenanceDTO.softwareIds() != null) {
                log.debug("Attaching {} software to maintenance record", maintenanceDTO.softwareIds().size());
                Set<Software> softwares = softwareService.softwareIdsToSoftwareSet(maintenanceDTO.softwareIds());
                maintenance.setSoftwares(softwares);
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
                throw new EntityNotFoundException("Maintenance with ID " + maintenanceId + " not found");
            }

            Maintenance maintenance = maintenanceOpt.get();

            updateMaintenanceName(maintenance, maintenanceDTO);
            updateMaintenanceDate(maintenance, maintenanceDTO);
            updateLastDate(maintenance, maintenanceDTO);
            updateMaintenanceComment(maintenance, maintenanceDTO);
            updateMaintenanceStatus(maintenance, maintenanceDTO);
            updateTimeSpent(maintenance, maintenanceDTO);
            updateBaitWorker(maintenance, maintenanceDTO);
            updateLocation(maintenance, maintenanceDTO);
            updateFiles(maintenance, maintenanceDTO);
            updateDevices(maintenance, maintenanceDTO);
            updateLinkedDevices(maintenance, maintenanceDTO);
            updateSoftware(maintenance, maintenanceDTO);

            maintenanceRepo.save(maintenance);

            log.info("Maintenance record updated successfully with ID: {}", maintenanceId);
            return new ResponseDTO("Maintenance updated successfully");
        } catch (Exception e) {
            log.error("Error while updating maintenance record with ID: {}", maintenanceId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO addTimeSpentToMaintenance(Integer maintenanceId, Integer hours, Integer minutes) {
        log.info("Adding spent time to maintenance with ID: {}", maintenanceId);
        try {
            Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);
            if (maintenanceOpt.isEmpty()) {
                log.warn("Maintenance record with ID {} not found", maintenanceId);
                throw new EntityNotFoundException("Maintenance with ID " + maintenanceId + " not found");
            }

            Maintenance maintenance = maintenanceOpt.get();

            Duration duration = maintenance.getTimeSpent();
            if (duration == null) {
                duration = Duration.ZERO;
            }

            if (hours != null) {
                duration = duration.plusHours(hours);
            }

            if (minutes != null) {
                duration = duration.plusMinutes(minutes);
            }

            maintenance.setTimeSpent(duration);
            maintenanceRepo.save(maintenance);
            return new ResponseDTO("Spent time added to maintenance successfully");
        } catch (Exception e) {
            log.error("Error while adding spent time to maintenance with ID: {}", maintenanceId, e);
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

    public void updateLastDate(Maintenance maintenance, MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.lastDate() != null) {
            maintenance.setLastDate(maintenanceDTO.lastDate());
        }
    }

    public void updateMaintenanceStatus(Maintenance maintenance, MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.maintenanceStatus() != null) {
            maintenance.setMaintenanceStatus(maintenanceDTO.maintenanceStatus());
        }
    }

    public void updateTimeSpent(Maintenance maintenance, MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.timeSpent() != null) {
            maintenance.setTimeSpent(maintenanceDTO.timeSpent());
        }
    }

    public void updateBaitWorker(Maintenance maintenance, MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.baitWorkerId() != null) {
            Optional<BaitWorker> baitWorkerOpt = baitWorkerRepo.findById(maintenanceDTO.baitWorkerId());
            baitWorkerOpt.ifPresent(maintenance::setBaitWorker);
        }
    }

    public void updateLocation(Maintenance maintenance, MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.locationId() != null) {
            Optional<Location> locationOpt = locationRepo.findById(maintenanceDTO.locationId());
            locationOpt.ifPresent(maintenance::setLocation);
        }
    }

    public void updateFiles(Maintenance maintenance, MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.fileIds() != null) {
            log.debug("Updating attached files for maintenance record");
            Set<FileUpload> files = fileUploadService.fileIdsToFilesSet(maintenanceDTO.fileIds());
            maintenance.setFiles(files);
        }
    }

    public void updateDevices(Maintenance maintenance, MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.deviceIds() != null) {
            log.debug("Updating attached devices for maintenance record");
            Set<Device> devices = deviceHelperService.deviceIdsToDevicesSet(maintenanceDTO.deviceIds());
            maintenance.setDevices(devices);
        }
    }

    public void updateLinkedDevices(Maintenance maintenance, MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.linkedDeviceIds() != null) {
            log.debug("Updating attached linked devices for maintenance record");
            Set<LinkedDevice> linkedDevices = linkedDeviceService.linkedDeviceIdsToLinkedDeviceSet(
                    maintenanceDTO.linkedDeviceIds());
            maintenance.setLinkedDevices(linkedDevices);
        }
    }

    public void updateSoftware(Maintenance maintenance, MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.softwareIds() != null) {
            log.debug("Updating attached software for maintenance record");
            Set<Software> software = softwareService.softwareIdsToSoftwareSet(maintenanceDTO.softwareIds());
            maintenance.setSoftwares(software);
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
        if (id == null) {
            log.warn("Maintenance ID is null. Returning null.");
            return null;
        }

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

    public Map<String, List<?>> getMaintenanceConnectionsMap(Integer maintenanceId) {
        if (maintenanceId == null) {
            log.warn("Maintenance ID is null. Returning empty map.");
            return Collections.emptyMap();
        }

        log.info("Fetching maintenance connections for Devices, Linked Devices and Software with maintenance ID: {}",
                maintenanceId);
        try {
            Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);
            if (maintenanceOpt.isEmpty()) {
                log.warn("Maintenance record with ID {} not found", maintenanceId);
                throw new EntityNotFoundException("Maintenance with " + maintenanceId + " not found");
            }
            Maintenance maintenance = maintenanceOpt.get();

            Map<String, List<?>> connections = new HashMap<>();

            connections.put("Devices", deviceMapper.toDtoList(new ArrayList<>(maintenance.getDevices())));
            connections.put("LinkedDevices", linkedDeviceMapper.toDtoList(new ArrayList<>(maintenance.getLinkedDevices())));
            connections.put("Software", softwareMapper.toDtoList(new ArrayList<>(maintenance.getSoftwares())));

            log.info("Maintenance connections map built: {}", connections);
            return connections;
        } catch (Exception e) {
            log.error("Error while fetching maintenance connections with ID: {}", maintenanceId, e);
            throw e;
        }
    }
}
