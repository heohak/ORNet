package com.demo.bait.service.DeviceServices;

import com.demo.bait.dto.*;
import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.DeviceClassificator;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.DeviceMapper;
import com.demo.bait.mapper.TicketMapper;
import com.demo.bait.repository.*;
import com.demo.bait.repository.classificator.DeviceClassificatorRepo;
import com.demo.bait.service.CommentServices.CommentService;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import com.demo.bait.service.MaintenanceServices.MaintenanceSpecificationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceService {

    private DeviceRepo deviceRepo;
    private DeviceMapper deviceMapper;
    private ClientRepo clientRepo;
    private LocationRepo locationRepo;
    private DeviceClassificatorRepo deviceClassificatorRepo;
    private FileUploadService fileUploadService;
    private CommentService commentService;
//    private DeviceMaintenanceService deviceMaintenanceService;
    private EntityManager entityManager;
    private DeviceAttributeService deviceAttributeService;
    private DeviceCommentService deviceCommentService;
    private TicketRepo ticketRepo;
    private TicketMapper ticketMapper;
    private MaintenanceSpecificationService maintenanceSpecificationService;

    @Transactional
    public ResponseDTO addDevice(DeviceDTO deviceDTO) {
        log.info("Adding a new device: {}", deviceDTO);
        try {
            Device device = new Device();

            updateClient(device, deviceDTO);
            updateLocation(device, deviceDTO);

            device.setDeviceName(deviceDTO.deviceName());
            device.setDepartment(deviceDTO.department());
            device.setRoom(deviceDTO.room());
            device.setSerialNumber(deviceDTO.serialNumber());
            device.setLicenseNumber(deviceDTO.licenseNumber());
            device.setVersion(deviceDTO.version());
            device.setVersionUpdateDate(deviceDTO.versionUpdateDate());

//            deviceMaintenanceService.updateMaintenances(device, deviceDTO);

            device.setFirstIPAddress(deviceDTO.firstIPAddress());
            device.setSecondIPAddress(deviceDTO.secondIPAddress());
            device.setSubnetMask(deviceDTO.subnetMask());
            device.setSoftwareKey(deviceDTO.softwareKey());
            device.setIntroducedDate(deviceDTO.introducedDate());
            device.setWrittenOffDate(deviceDTO.writtenOffDate());
            device.setWorkstationNo(deviceDTO.workstationNo());
            device.setCameraNo(deviceDTO.cameraNo());
            device.setOtherNo(deviceDTO.otherNo());

            if (deviceDTO.commentIds() != null) {
                log.debug("Adding comments with IDs: {}", deviceDTO.commentIds());
                Set<Comment> comments = commentService.commentIdsToCommentsSet(deviceDTO.commentIds());
                device.setComments(comments);
            }

            if (deviceDTO.fileIds() != null) {
                log.debug("Adding files with IDs: {}", deviceDTO.fileIds());
                Set<FileUpload> files = fileUploadService.fileIdsToFilesSet(deviceDTO.fileIds());
                device.setFiles(files);
            }

            if (deviceDTO.attributes() != null) {
                log.debug("Setting custom attributes: {}", deviceDTO.attributes());
                device.setAttributes(deviceDTO.attributes());
            } else {
                device.setAttributes(new HashMap<>());
            }

            updateDeviceClassificator(device, deviceDTO);

            deviceRepo.save(device);
            log.info("Successfully added device with ID: {}", device.getId());
            return new ResponseDTO(device.getId().toString());
        } catch (Exception e) {
            log.error("Error while adding device: {}", deviceDTO, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO addLocationToDevice(Integer deviceId, Integer locationId) {
        log.info("Adding location with ID: {} to device with ID: {}", locationId, deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            Optional<Location> locationOpt = locationRepo.findById(locationId);

            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }
            if (locationOpt.isEmpty()) {
                log.warn("Location with ID {} not found.", locationId);
                throw new EntityNotFoundException("Location with id " + locationId + " not found");
            }

            Device device = deviceOpt.get();
            Location location = locationOpt.get();
            device.setLocation(location);
            deviceRepo.save(device);

            log.info("Successfully added location with ID: {} to device with ID: {}", locationId, deviceId);
            return new ResponseDTO("Location added successfully to device");
        } catch (Exception e) {
            log.error("Error while adding location with ID: {} to device with ID: {}", locationId, deviceId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO addClientToDevice(Integer deviceId, Integer clientId) {
        log.info("Adding client with ID: {} to device with ID: {}", clientId, deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            Optional<Client> clientOpt = clientRepo.findById(clientId);

            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }
            if (clientOpt.isEmpty()) {
                log.warn("Client with ID {} not found.", clientId);
                throw new EntityNotFoundException("Client with id " + clientId + " not found");
            }

            Device device = deviceOpt.get();
            Client client = clientOpt.get();
            device.setClient(client);
            deviceRepo.save(device);

            log.info("Successfully added client with ID: {} to device with ID: {}", clientId, deviceId);
            return new ResponseDTO("Client added successfully");
        } catch (Exception e) {
            log.error("Error while adding client with ID: {} to device with ID: {}", clientId, deviceId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO addClassificatorToDevice(Integer deviceId, Integer classificatorId) {
        log.info("Adding classificator with ID: {} to device with ID: {}", classificatorId, deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            Optional<DeviceClassificator> deviceClassificatorOpt = deviceClassificatorRepo.findById(classificatorId);

            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }
            if (deviceClassificatorOpt.isEmpty()) {
                log.warn("Classificator with ID {} not found.", classificatorId);
                throw new EntityNotFoundException("Classificator with id " + classificatorId + " not found");
            }

            Device device = deviceOpt.get();
            DeviceClassificator deviceClassificator = deviceClassificatorOpt.get();
            log.debug("Assigning classificator with ID: {} to device with ID: {}", classificatorId, deviceId);
            device.setClassificator(deviceClassificator);
            deviceRepo.save(device);

            log.info("Successfully added classificator with ID: {} to device with ID: {}", classificatorId, deviceId);
            return new ResponseDTO("Classificator added successfully");
        } catch (Exception e) {
            log.error("Error while adding classificator with ID: {} to device with ID: {}", classificatorId, deviceId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO addWrittenOffDate(Integer deviceId, LocalDate writtenOffDate, String comment) {
        log.info("Adding written-off date to device with ID: {}", deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);

            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }

            Device device = deviceOpt.get();
            device.setWrittenOffDate(writtenOffDate);
            String commentText = (comment != null ? comment : "") + " (DEVICE WRITTEN-OFF)";
            log.debug("Adding comment: '{}' to device with ID: {}", commentText, deviceId);
            deviceCommentService.addCommentToDevice(deviceId, commentText);
            deviceRepo.save(device);

            log.info("Successfully added written-off date to device with ID: {}", deviceId);
            return new ResponseDTO("Written off date added successfully");
        } catch (Exception e) {
            log.error("Error while adding written-off date to device with ID: {}", deviceId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO reactivateDevice(Integer deviceId, String comment) {
        log.info("Reactivating device with ID: {}", deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }

            Device device = deviceOpt.get();

            if (device.getWrittenOffDate() == null) {
                log.warn("Device with ID {} has not been written off.", deviceId);
                throw new ApplicationContextException("Device with id " + deviceId + " has not been written off");
            }

            String commentText = (comment != null ? comment : "") + " (DEVICE REACTIVATED)";
            log.debug("Adding comment: '{}' to device with ID: {}", commentText, deviceId);
            deviceCommentService.addCommentToDevice(deviceId, commentText);

            device.setWrittenOffDate(null);
            deviceRepo.save(device);

            log.info("Successfully reactivated device with ID: {}", deviceId);
            return new ResponseDTO("Device reactivated");
        } catch (Exception e) {
            log.error("Error while reactivating device with ID: {}", deviceId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO deleteDevice(Integer deviceId) {
        log.info("Deleting device with ID: {}", deviceId);
        try {
            deviceRepo.deleteById(deviceId);
            log.info("Successfully deleted device with ID: {}", deviceId);
            return new ResponseDTO("Device deleted successfully");
        } catch (Exception e) {
            log.error("Error while deleting device with ID: {}", deviceId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO updateDevice(Integer deviceId, DeviceDTO deviceDTO) {
        log.info("Updating device with ID: {}", deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }

            Device device = deviceOpt.get();

            updateClient(device, deviceDTO);
            updateLocation(device, deviceDTO);
            updateDeviceName(device, deviceDTO);
            updateDepartment(device, deviceDTO);
            updateRoom(device, deviceDTO);
            updateSerialNumber(device, deviceDTO);
            updateLicenceNumber(device, deviceDTO);
            updateVersion(device, deviceDTO);
            updateVersionUpdateDate(device, deviceDTO);
//            deviceMaintenanceService.updateMaintenances(device, deviceDTO);
            updateFirstIPAddress(device, deviceDTO);
            updateSecondIPAddress(device, deviceDTO);
            updateSubnetMask(device, deviceDTO);
            updateSoftwareKey(device, deviceDTO);
            updateIntroducedDate(device, deviceDTO);
            updateWrittenOffDate(device, deviceDTO);
            deviceAttributeService.updateDeviceAttributes(deviceId, deviceDTO.attributes());

            updateDeviceClassificator(device, deviceDTO);

            updateWorkstationNo(device, deviceDTO);
            updateCameraNo(device, deviceDTO);
            updateOtherNo(device, deviceDTO);

            deviceRepo.save(device);
            log.info("Successfully updated device with ID: {}", deviceId);
            return new ResponseDTO("Device updated successfully");
        } catch (Exception e) {
            log.error("Error while updating device with ID: {}", deviceId, e);
            throw e;
        }
    }

    public void updateWorkstationNo(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.workstationNo() != null) {
            device.setWorkstationNo(deviceDTO.workstationNo());
        }
    }

    public void updateCameraNo(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.cameraNo() != null) {
            device.setCameraNo(deviceDTO.cameraNo());
        }
    }

    public void updateOtherNo(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.otherNo() != null) {
            device.setOtherNo(deviceDTO.otherNo());
        }
    }

    public void updateClient(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.clientId() != null) {
            Optional<Client> clientOpt = clientRepo.findById(deviceDTO.clientId());
            clientOpt.ifPresent(device::setClient);
        }
    }

    public void updateLocation(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.locationId() != null) {
            Optional<Location> locationOpt = locationRepo.findById(deviceDTO.locationId());
            locationOpt.ifPresent(device::setLocation);
        }
    }

    public void updateDeviceName(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.deviceName() != null) {
            device.setDeviceName(deviceDTO.deviceName());
        }
    }

    public void updateDepartment(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.department() != null) {
            device.setDepartment(deviceDTO.department());
        }
    }

    public void updateRoom(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.room() != null) {
            device.setRoom(deviceDTO.room());
        }
    }

    public void updateSerialNumber(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.serialNumber() != null) {
            device.setSerialNumber(deviceDTO.serialNumber());
        }
    }

    public void updateLicenceNumber(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.licenseNumber() != null) {
            device.setLicenseNumber(deviceDTO.licenseNumber());
        }
    }

    public void updateVersion(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.version() != null) {
            device.setVersion(deviceDTO.version());
        }
    }

    public void updateVersionUpdateDate(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.versionUpdateDate() != null) {
            device.setVersionUpdateDate(deviceDTO.versionUpdateDate());
        }
    }

    public void updateFirstIPAddress(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.firstIPAddress() != null) {
            device.setFirstIPAddress(deviceDTO.firstIPAddress());
        }
    }

    public void updateSecondIPAddress(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.secondIPAddress() != null) {
            device.setSecondIPAddress(deviceDTO.secondIPAddress());
        }
    }

    public void updateSubnetMask(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.subnetMask() != null) {
            device.setSubnetMask(deviceDTO.subnetMask());
        }
    }

    public void updateSoftwareKey(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.softwareKey() != null) {
            device.setSoftwareKey(deviceDTO.softwareKey());
        }
    }

    public void updateIntroducedDate(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.introducedDate() != null) {
            device.setIntroducedDate(deviceDTO.introducedDate());
        }
    }

    public void updateWrittenOffDate(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.writtenOffDate() != null) {
            device.setWrittenOffDate(deviceDTO.writtenOffDate());
        }
    }

    public void updateDeviceClassificator(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.classificatorId() != null) {
            Optional<DeviceClassificator> deviceClassificatorOpt = deviceClassificatorRepo
                    .findById(deviceDTO.classificatorId());
            deviceClassificatorOpt.ifPresent(device::setClassificator);
        }
    }

    public List<DeviceDTO> getDevicesByClientId(Integer clientId) {
        if (clientId == null) {
            log.warn("Client ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        log.info("Fetching devices for client with ID: {}", clientId);
        try {
            List<DeviceDTO> devices = deviceMapper.toDtoList(deviceRepo.findByClientId(clientId));
            log.info("Fetched {} devices for client with ID: {}", devices.size(), clientId);
            return devices;
        } catch (Exception e) {
            log.error("Error while fetching devices for client with ID: {}", clientId, e);
            throw e;
        }
    }

    public List<DeviceDTO> getAllDevices() {
        log.info("Fetching all devices.");
        try {
            List<DeviceDTO> devices = deviceMapper.toDtoList(deviceRepo.findAll());
            log.info("Fetched {} devices.", devices.size());
            return devices;
        } catch (Exception e) {
            log.error("Error while fetching all devices.", e);
            throw e;
        }
    }

    public DeviceDTO getDeviceById(Integer deviceId) {
        if (deviceId == null) {
            log.warn("Device ID is null. Returning null.");
            return null;
        }

        log.info("Fetching device with ID: {}", deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }
            Device device = deviceOpt.get();
            log.info("Fetched device with ID: {}", deviceId);
            return deviceMapper.toDto(device);
        } catch (Exception e) {
            log.error("Error while fetching device with ID: {}", deviceId, e);
            throw e;
        }
    }

    public List<DeviceDTO> getDeviceHistory(Integer deviceId) {
        if (deviceId == null) {
            log.warn("Device ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        log.info("Fetching history for device with ID: {}", deviceId);
        try {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            List<Number> revisions = auditReader.getRevisions(Device.class, deviceId);

            List<Device> history = new ArrayList<>();
            for (Number rev : revisions) {
                Device deviceVersion = auditReader.find(Device.class, deviceId, rev);
                history.add(deviceVersion);
            }

            log.info("Fetched {} revisions for device with ID: {}", history.size(), deviceId);
            return deviceMapper.toDtoList(history);
        } catch (Exception e) {
            log.error("Error while fetching history for device with ID: {}", deviceId, e);
            throw e;
        }
    }

    public List<TicketDTO> getDeviceTickets(Integer deviceId) {
        if (deviceId == null) {
            log.warn("Device ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        log.info("Fetching tickets for device with ID: {}", deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }

            Device device = deviceOpt.get();
            List<Ticket> tickets = ticketRepo.findByDevicesContains(device);
            log.info("Fetched {} tickets for device with ID: {}", tickets.size(), deviceId);
            return ticketMapper.toDtoList(tickets);
        } catch (Exception e) {
            log.error("Error while fetching tickets for device with ID: {}", deviceId, e);
            throw e;
        }
    }

    public List<MaintenanceDTO> getDeviceMaintenances(Integer deviceId) {
        if (deviceId == null) {
            log.warn("Device ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }

            return maintenanceSpecificationService.searchAndFilterMaintenances(null, null, null,
                    deviceId, null, null, null);
        } catch (Exception e) {
            log.error("Error while fetching maintenances for device with ID: {}", deviceId, e);
            throw e;
        }
    }
}
