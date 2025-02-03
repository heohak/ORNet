package com.demo.bait.service.LinkedDeviceServices;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.LinkedDeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.LinkedDevice;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.CommentMapper;
import com.demo.bait.mapper.DeviceMapper;
import com.demo.bait.mapper.LinkedDeviceMapper;
import com.demo.bait.repository.CommentRepo;
import com.demo.bait.repository.DeviceRepo;
import com.demo.bait.repository.LinkedDeviceRepo;
import com.demo.bait.repository.LocationRepo;
import com.demo.bait.service.CommentServices.CommentService;
import com.demo.bait.service.DeviceServices.DeviceService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class LinkedDeviceService {

    private LinkedDeviceRepo linkedDeviceRepo;
    private LinkedDeviceMapper linkedDeviceMapper;
    private DeviceRepo deviceRepo;
    private CommentService commentService;
    private EntityManager entityManager;
    private DeviceMapper deviceMapper;
    private LocationRepo locationRepo;

    @Transactional
    public ResponseDTO addLinkedDevice(LinkedDeviceDTO linkedDeviceDTO) {
        log.info("Adding a new linked device with details: {}", linkedDeviceDTO);
        try {
            LinkedDevice linkedDevice = new LinkedDevice();
            updateDevice(linkedDevice, linkedDeviceDTO);

            linkedDevice.setName(linkedDeviceDTO.name());
            linkedDevice.setManufacturer(linkedDeviceDTO.manufacturer());
            linkedDevice.setProductCode(linkedDeviceDTO.productCode());
            linkedDevice.setSerialNumber(linkedDeviceDTO.serialNumber());

            if (linkedDeviceDTO.commentIds() != null) {
                log.debug("Adding comments to linked device");
                Set<Comment> comments = commentService.commentIdsToCommentsSet(linkedDeviceDTO.commentIds());
                linkedDevice.setComments(comments);
            }

            linkedDevice.setAttributes(linkedDeviceDTO.attributes() != null
                    ? linkedDeviceDTO.attributes()
                    : new HashMap<>());

            linkedDevice.setDescription(linkedDeviceDTO.description());
            linkedDevice.setIntroducedDate(linkedDeviceDTO.introducedDate());
            updateLocation(linkedDevice, linkedDeviceDTO);
            linkedDevice.setTemplate(linkedDeviceDTO.template());

            linkedDeviceRepo.save(linkedDevice);
            log.info("Linked device added successfully with ID: {}", linkedDevice.getId());
            return new ResponseDTO(linkedDevice.getId().toString());
        } catch (Exception e) {
            log.error("Error while adding a linked device", e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO linkDevice(Integer linkedDeviceId, Integer deviceId) {
        log.info("Linking device with ID: {} to linked device with ID: {}", deviceId, linkedDeviceId);
        try {
            LinkedDevice linkedDevice = linkedDeviceRepo.findById(linkedDeviceId)
                    .orElseThrow(() -> {
                        log.warn("Linked Device with ID {} not found", linkedDeviceId);
                        return new EntityNotFoundException("Linked Device with id " + linkedDeviceId + " not found");
                    });
            Device device = deviceRepo.findById(deviceId)
                    .orElseThrow(() -> {
                        log.warn("Device with ID {} not found", deviceId);
                        return new EntityNotFoundException("Device with id " + deviceId + " not found");
                    });

            linkedDevice.setDevice(device);
            linkedDeviceRepo.save(linkedDevice);
            log.info("Device linked successfully to linked device ID: {}", linkedDeviceId);
            return new ResponseDTO("Device linked successfully");
        } catch (Exception e) {
            log.error("Error while linking device to linked device", e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO deleteLinkedDevice(Integer linkedDeviceId) {
        log.info("Deleting linked device with ID: {}", linkedDeviceId);
        try {
            linkedDeviceRepo.deleteById(linkedDeviceId);
            log.info("Linked device with ID: {} deleted successfully", linkedDeviceId);
            return new ResponseDTO("Linked Device deleted");
        } catch (Exception e) {
            log.error("Error while deleting linked device with ID: {}", linkedDeviceId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO updateLinkedDevice(Integer linkedDeviceId, LinkedDeviceDTO linkedDeviceDTO) {
        log.info("Updating linked device with ID: {}", linkedDeviceId);
        try {
            LinkedDevice linkedDevice = linkedDeviceRepo.findById(linkedDeviceId)
                    .orElseThrow(() -> {
                        log.warn("Linked Device with ID {} not found", linkedDeviceId);
                        return new EntityNotFoundException("Linked Device with id " + linkedDeviceId + " not found");
                    });

            updateDevice(linkedDevice, linkedDeviceDTO);
            updateName(linkedDevice, linkedDeviceDTO);
            updateManufacturer(linkedDevice, linkedDeviceDTO);
            updateProductCode(linkedDevice, linkedDeviceDTO);
            updateSerialNumber(linkedDevice, linkedDeviceDTO);

            updateDescription(linkedDevice, linkedDeviceDTO);
            updateIntroducedDate(linkedDevice, linkedDeviceDTO);
            updateLocation(linkedDevice, linkedDeviceDTO);
            updateTemplate(linkedDevice, linkedDeviceDTO);

            linkedDeviceRepo.save(linkedDevice);
            log.info("Linked device with ID: {} updated successfully", linkedDeviceId);
            return new ResponseDTO("Linked Device updated successfully");
        } catch (Exception e) {
            log.error("Error while updating linked device with ID: {}", linkedDeviceId, e);
            throw e;
        }
    }

    public void updateLocation(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.locationId() != null) {
            Optional<Location> locationOpt = locationRepo.findById(linkedDeviceDTO.locationId());
            locationOpt.ifPresent(linkedDevice::setLocation);
        }
    }

    public void updateDescription(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.description() != null) {
            linkedDevice.setDescription(linkedDeviceDTO.description());
        }
    }

    public void updateIntroducedDate(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.introducedDate() != null) {
            linkedDevice.setIntroducedDate(linkedDeviceDTO.introducedDate());
        }
    }

    public void updateTemplate(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.template() != null) {
            linkedDevice.setTemplate(linkedDeviceDTO.template());
        }
    }
    public void updateDevice(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.deviceId() != null) {
            Optional<Device> deviceOpt = deviceRepo.findById(linkedDeviceDTO.deviceId());
            deviceOpt.ifPresent(linkedDevice::setDevice);
        }
    }

    public void updateName(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.name() != null) {
            linkedDevice.setName(linkedDeviceDTO.name());
        }
    }

    public void updateManufacturer(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.manufacturer() != null) {
            linkedDevice.setManufacturer(linkedDeviceDTO.manufacturer());
        }
    }

    public void updateProductCode(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.productCode() != null) {
            linkedDevice.setProductCode(linkedDeviceDTO.productCode());
        }
    }

    public void updateSerialNumber(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.serialNumber() != null) {
            linkedDevice.setSerialNumber(linkedDeviceDTO.serialNumber());
        }
    }

    public List<LinkedDeviceDTO> getNotUsedLinkedDevices() {
        log.info("Fetching all linked devices not associated with any device and with template set to false");
        List<LinkedDeviceDTO> devices = linkedDeviceMapper.toDtoList(
                linkedDeviceRepo.findByDeviceIsNullAndTemplateFalse()
        );
        log.info("Fetched {} linked devices not associated with any device and with template set to false", devices.size());
        return devices;
    }

    public List<LinkedDeviceDTO> getAllLinkedDevices() {
        log.info("Fetching all linked devices");
        List<LinkedDeviceDTO> devices = linkedDeviceMapper.toDtoList(linkedDeviceRepo.findAll());
        log.info("Fetched {} linked devices", devices.size());
        return devices;
    }

    public List<LinkedDeviceDTO> getLinkedDevicesByDeviceId(Integer deviceId) {
        if (deviceId == null) {
            log.warn("Device ID is null. Returning empty list");
            return Collections.emptyList();
        }

        log.info("Fetching linked devices for device ID: {}", deviceId);
        List<LinkedDeviceDTO> devices = linkedDeviceMapper.toDtoList(linkedDeviceRepo.findByDeviceId(deviceId));
        log.info("Fetched {} linked devices for device ID: {}", devices.size(), deviceId);
        return devices;
    }

    public List<LinkedDeviceDTO> getLinkedDeviceHistory(Integer linkedDeviceId) {
        if (linkedDeviceId == null) {
            log.warn("Linked device ID is null. Returning empty list");
            return Collections.emptyList();
        }

        log.info("Fetching history for linked device ID: {}", linkedDeviceId);
        try {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            List<Number> revisions = auditReader.getRevisions(LinkedDevice.class, linkedDeviceId);

            List<LinkedDevice> history = new ArrayList<>();
            for (Number rev : revisions) {
                LinkedDevice version = auditReader.find(LinkedDevice.class, linkedDeviceId, rev);
                history.add(version);
            }

            log.info("Fetched history with {} versions for linked device ID: {}", history.size(), linkedDeviceId);
            return linkedDeviceMapper.toDtoList(history);
        } catch (Exception e) {
            log.error("Error while fetching history for linked device ID: {}", linkedDeviceId, e);
            throw e;
        }
    }

    public DeviceDTO getLinkedDeviceDevice(Integer linkedDeviceId) {
        if (linkedDeviceId == null) {
            log.warn("Linked device ID is null. Returning null");
            return null;
        }

        log.info("Fetching associated device for linked device ID: {}", linkedDeviceId);
        LinkedDevice linkedDevice = linkedDeviceRepo.findById(linkedDeviceId)
                .orElseThrow(() -> {
                    log.warn("Linked Device with ID {} not found", linkedDeviceId);
                    return new EntityNotFoundException("Linked Device with id " + linkedDeviceId + " not found");
                });
        DeviceDTO device = deviceMapper.toDto(linkedDevice.getDevice());
        log.info("Fetched associated device for linked device ID: {}", linkedDeviceId);
        return device;
    }

    @Transactional
    public ResponseDTO removeDeviceFromLinkedDevice(Integer linkedDeviceId) {
        log.info("Removing device association from linked device ID: {}", linkedDeviceId);
        try {
            LinkedDevice linkedDevice = linkedDeviceRepo.findById(linkedDeviceId)
                    .orElseThrow(() -> {
                        log.warn("Linked Device with ID {} not found", linkedDeviceId);
                        return new EntityNotFoundException("Linked Device with id " + linkedDeviceId + " not found");
                    });

            linkedDevice.setDevice(null);
            linkedDeviceRepo.save(linkedDevice);
            log.info("Device association removed successfully from linked device ID: {}", linkedDeviceId);
            return new ResponseDTO("Device removed from linked device successfully");
        } catch (Exception e) {
            log.error("Error while removing device association from linked device ID: {}", linkedDeviceId, e);
            throw e;
        }
    }
}
