package com.demo.bait.service.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.DeviceClassificatorDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.classificator.DeviceClassificator;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.classificator.DeviceClassificatorMapper;
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

    @Transactional
    public DeviceClassificatorDTO addDeviceClassificator(DeviceClassificatorDTO deviceClassificatorDTO) {
        DeviceClassificator deviceClassificator = new DeviceClassificator();
        deviceClassificator.setName(deviceClassificatorDTO.name());
        deviceClassificatorRepo.save(deviceClassificator);
        return deviceClassificatorMapper.toDto(deviceClassificator);
    }

    @Transactional
    public ResponseDTO updateDeviceClassificator(Integer deviceClassificatorId,
                                                 DeviceClassificatorDTO deviceClassificatorDTO) {
        Optional<DeviceClassificator> deviceClassificatorOpt = deviceClassificatorRepo.findById(deviceClassificatorId);
        if (deviceClassificatorOpt.isEmpty()) {
            throw new EntityNotFoundException("Device classificator with id " + deviceClassificatorId + " not found");
        }
        DeviceClassificator classificator = deviceClassificatorOpt.get();
        if (deviceClassificatorDTO.name() != null) {
            classificator.setName(deviceClassificatorDTO.name());
        }
        deviceClassificatorRepo.save(classificator);
        return new ResponseDTO("Device classificator updated successfully");
    }

    public List<DeviceClassificatorDTO> getAllClassificators() {
        return deviceClassificatorMapper.toDtoList(deviceClassificatorRepo.findAll());
    }

    @Transactional
    public ResponseDTO deleteDeviceClassificator(Integer deviceClassificatorId) {
        deviceClassificatorRepo.deleteById(deviceClassificatorId);
        return new ResponseDTO("Device classificator deleted successfully");
    }

    @Transactional
    public DeviceClassificatorDTO getDeviceClassificatorById(Integer deviceClassificatorId) {
        Optional<DeviceClassificator> deviceClassificatorOpt = deviceClassificatorRepo.findById(deviceClassificatorId);
        if (deviceClassificatorOpt.isEmpty()) {
            throw new EntityNotFoundException("Device classificator with id " + deviceClassificatorId + " not found");
        }
        return deviceClassificatorMapper.toDto(deviceClassificatorOpt.get());
    }

    public List<DeviceClassificatorDTO> getDeviceClassificatorHistory(Integer deviceClassificatorId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(DeviceClassificator.class, deviceClassificatorId);

        List<DeviceClassificator> history = new ArrayList<>();
        for (Number rev : revisions) {
            DeviceClassificator deviceClassificatorVersion = auditReader
                    .find(DeviceClassificator.class, deviceClassificatorId, rev);
            history.add(deviceClassificatorVersion);
        }
        return deviceClassificatorMapper.toDtoList(history);
    }

    public List<DeviceClassificatorDTO> getDeletedDeviceClassificators() {
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

        return deviceClassificatorMapper.toDtoList(deletedEntities);
    }
}
