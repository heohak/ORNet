package com.demo.bait.service.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.DeviceClassificatorDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.classificator.DeviceClassificator;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.classificator.DeviceClassificatorMapper;
import com.demo.bait.repository.DeviceRepo;
import com.demo.bait.repository.classificator.DeviceClassificatorRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceClassificatorService {

    private DeviceClassificatorRepo deviceClassificatorRepo;
    private DeviceClassificatorMapper deviceClassificatorMapper;
    private EntityManager entityManager;
    private DeviceRepo deviceRepo;

    @Transactional
    public DeviceClassificatorDTO addDeviceClassificator(DeviceClassificatorDTO deviceClassificatorDTO) {
        log.info("Adding new Device Classificator: {}", deviceClassificatorDTO);
        try {
            DeviceClassificator deviceClassificator = new DeviceClassificator();
            deviceClassificator.setName(deviceClassificatorDTO.name());
            deviceClassificatorRepo.save(deviceClassificator);
            log.info("Successfully added Device Classificator: {}", deviceClassificator);
            return deviceClassificatorMapper.toDto(deviceClassificator);
        } catch (Exception e) {
            log.error("Error while adding Device Classificator: {}", deviceClassificatorDTO, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO updateDeviceClassificator(Integer deviceClassificatorId, DeviceClassificatorDTO deviceClassificatorDTO) {
        log.info("Updating Device Classificator with ID: {}", deviceClassificatorId);
        try {
            Optional<DeviceClassificator> deviceClassificatorOpt = deviceClassificatorRepo.findById(deviceClassificatorId);
            if (deviceClassificatorOpt.isEmpty()) {
                log.warn("Device Classificator with ID {} not found", deviceClassificatorId);
                throw new EntityNotFoundException("Device classificator with id " + deviceClassificatorId + " not found");
            }
            DeviceClassificator classificator = deviceClassificatorOpt.get();
            if (deviceClassificatorDTO.name() != null) {
                classificator.setName(deviceClassificatorDTO.name());
            }
            deviceClassificatorRepo.save(classificator);
            log.info("Successfully updated Device Classificator with ID: {}", deviceClassificatorId);
            return new ResponseDTO("Device classificator updated successfully");
        } catch (Exception e) {
            log.error("Error while updating Device Classificator with ID: {}", deviceClassificatorId, e);
            throw e;
        }
    }

    public List<DeviceClassificatorDTO> getAllClassificators() {
        log.info("Fetching all Device Classificators");
        try {
            List<DeviceClassificatorDTO> result = deviceClassificatorMapper.toDtoList(deviceClassificatorRepo.findAll());
            log.debug("Fetched Device Classificators: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error while fetching all Device Classificators", e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO deleteDeviceClassificator(Integer deviceClassificatorId) {
        log.info("Deleting Device Classificator with ID: {}", deviceClassificatorId);
        try {
            Optional<DeviceClassificator> classificatorOpt = deviceClassificatorRepo.findById(deviceClassificatorId);
            if (classificatorOpt.isEmpty()) {
                log.warn("Device Classificator with ID {} not found", deviceClassificatorId);
                throw new EntityNotFoundException("Device classificator with id " + deviceClassificatorId + " not found");
            }
            DeviceClassificator classificator = classificatorOpt.get();

            log.debug("Unlinking associated devices for Classificator ID: {}", deviceClassificatorId);
            List<Device> associatedDevices = deviceRepo.findByClassificator(classificator);
            for (Device device : associatedDevices) {
                device.setClassificator(null);
                deviceRepo.save(device);
            }

            deviceClassificatorRepo.delete(classificator);
            log.info("Successfully deleted Device Classificator with ID: {}", deviceClassificatorId);
            return new ResponseDTO("Device classificator deleted successfully");
        } catch (Exception e) {
            log.error("Error while deleting Device Classificator with ID: {}", deviceClassificatorId, e);
            throw e;
        }
    }

    @Transactional
    public DeviceClassificatorDTO getDeviceClassificatorById(Integer deviceClassificatorId) {
        if (deviceClassificatorId == null) {
            log.warn("Device Classificator ID is null. Returning null");
            return null;
        }

        log.info("Fetching Device Classificator with ID: {}", deviceClassificatorId);
        try {
            Optional<DeviceClassificator> deviceClassificatorOpt = deviceClassificatorRepo.findById(deviceClassificatorId);
            if (deviceClassificatorOpt.isEmpty()) {
                log.warn("Device Classificator with ID {} not found", deviceClassificatorId);
                throw new EntityNotFoundException("Device classificator with id " + deviceClassificatorId + " not found");
            }
            DeviceClassificatorDTO result = deviceClassificatorMapper.toDto(deviceClassificatorOpt.get());
            log.debug("Fetched Device Classificator with ID {}: {}", deviceClassificatorId, result);
            return result;
        } catch (Exception e) {
            log.error("Error while fetching Device Classificator with ID: {}", deviceClassificatorId, e);
            throw e;
        }
    }

    public List<DeviceClassificatorDTO> getDeviceClassificatorHistory(Integer deviceClassificatorId) {
        if (deviceClassificatorId == null) {
            log.warn("Device Classificator ID is null. Returning empty list");
            return Collections.emptyList();
        }

        log.info("Fetching history for Device Classificator with ID: {}", deviceClassificatorId);
        try {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            List<Number> revisions = auditReader.getRevisions(DeviceClassificator.class, deviceClassificatorId);

            List<DeviceClassificator> history = new ArrayList<>();
            for (Number rev : revisions) {
                DeviceClassificator deviceClassificatorVersion = auditReader
                        .find(DeviceClassificator.class, deviceClassificatorId, rev);
                history.add(deviceClassificatorVersion);
            }
            List<DeviceClassificatorDTO> result = deviceClassificatorMapper.toDtoList(history);
            log.debug("Fetched history for Device Classificator with ID {}: {}", deviceClassificatorId, result);
            return result;
        } catch (Exception e) {
            log.error("Error while fetching history for Device Classificator with ID: {}", deviceClassificatorId, e);
            throw e;
        }
    }

    public List<DeviceClassificatorDTO> getDeletedDeviceClassificators() {
        log.info("Fetching deleted Device Classificators");
        try {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);

            AuditQuery query = auditReader.createQuery()
                    .forRevisionsOfEntity(DeviceClassificator.class, false, true)
                    .add(AuditEntity.revisionType().eq(org.hibernate.envers.RevisionType.DEL));

            List<Object[]> result = query.getResultList();

            List<DeviceClassificator> deletedEntities = result.stream()
                    .map(r -> {
                        DeviceClassificator deletedEntity = (DeviceClassificator) r[0];
                        DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) r[1];

                        DeviceClassificator lastStateBeforeDeletion = auditReader.find(
                                DeviceClassificator.class,
                                deletedEntity.getId(),
                                revisionEntity.getId() - 1
                        );

                        return lastStateBeforeDeletion != null ? lastStateBeforeDeletion : deletedEntity;
                    })
                    .collect(Collectors.toList());

            List<DeviceClassificatorDTO> deletedDTOs = deviceClassificatorMapper.toDtoList(deletedEntities);
            log.debug("Fetched deleted Device Classificators: {}", deletedDTOs);
            return deletedDTOs;
        } catch (Exception e) {
            log.error("Error while fetching deleted Device Classificators", e);
            throw e;
        }
    }
}
