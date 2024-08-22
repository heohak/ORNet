package com.demo.bait.service.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.WorkTypeClassificatorDTO;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.classificator.WorkTypeClassificatorMapper;
import com.demo.bait.repository.classificator.WorkTypeClassificatorRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import jakarta.persistence.EntityManager;

@Slf4j
@Service
@AllArgsConstructor
public class WorkTypeClassificatorService {

    private WorkTypeClassificatorRepo workTypeClassificatorRepo;
    private WorkTypeClassificatorMapper workTypeClassificatorMapper;
    private EntityManager entityManager;

    @Transactional
    public ResponseDTO addWorkTypeClassificator(WorkTypeClassificatorDTO workTypeClassificatorDTO) {
        WorkTypeClassificator workTypeClassificator = new WorkTypeClassificator();
        workTypeClassificator.setWorkType(workTypeClassificatorDTO.workType());
        workTypeClassificatorRepo.save(workTypeClassificator);
        return new ResponseDTO("Work Type classificator added successfully");
    }

    @Transactional
    public ResponseDTO updateWorkTypeClassificator(Integer workTypeId,
                                                   WorkTypeClassificatorDTO workTypeClassificatorDTO) {
        Optional<WorkTypeClassificator> workTypeOpt = workTypeClassificatorRepo.findById(workTypeId);
        if(workTypeOpt.isEmpty()) {
            throw new EntityNotFoundException("Work Type classificator witn id " + workTypeId + " not found");
        }
        WorkTypeClassificator workType = workTypeOpt.get();

        if (workTypeClassificatorDTO.workType() != null) {
            workType.setWorkType(workTypeClassificatorDTO.workType());
        }
        workTypeClassificatorRepo.save(workType);
        return new ResponseDTO("Work type classificator updated successfully");
    }

    @Transactional
    public ResponseDTO deleteWorkTypeClassificator(Integer workTypeId) {
        workTypeClassificatorRepo.deleteById(workTypeId);
        return new ResponseDTO("Work type deleted successfully");
    }

    public List<WorkTypeClassificatorDTO> getAllWorkTypes() {
        return workTypeClassificatorMapper.toDtoList(workTypeClassificatorRepo.findAll());
    }

    public Set<WorkTypeClassificator> workTypeIdsToWorkTypesSet(List<Integer> workTypeIds) {
        Set<WorkTypeClassificator> workTypes = new HashSet<>();
        for (Integer id : workTypeIds) {
            WorkTypeClassificator workType = workTypeClassificatorRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid work type classificator ID: " + id));
            workTypes.add(workType);
        }
        return workTypes;
    }

    public List<WorkTypeClassificatorDTO> getWorkTypeHistory(Integer workTypeId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(WorkTypeClassificator.class, workTypeId);

        List<WorkTypeClassificator> history = new ArrayList<>();
        for (Number rev : revisions) {
            WorkTypeClassificator workTypeClassificatorVersion = auditReader
                    .find(WorkTypeClassificator.class, workTypeId, rev);
            history.add(workTypeClassificatorVersion);
        }
        return workTypeClassificatorMapper.toDtoList(history);
    }

    public List<WorkTypeClassificatorDTO> getDeletedEntities() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        AuditQuery query = auditReader.createQuery()
                .forRevisionsOfEntity(WorkTypeClassificator.class, false, true)
                .add(AuditEntity.revisionType().eq(org.hibernate.envers.RevisionType.DEL));

        List<Object[]> result = query.getResultList();

        List<WorkTypeClassificator> deletedEntities = result.stream()
                .map(r -> {
                    WorkTypeClassificator deletedEntity = (WorkTypeClassificator) r[0];
                    DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) r[1];

                    WorkTypeClassificator lastStateBeforeDeletion = auditReader.find(
                            WorkTypeClassificator.class,
                            deletedEntity.getId(),
                            revisionEntity.getId() - 1);

                    return lastStateBeforeDeletion != null ? lastStateBeforeDeletion : deletedEntity;
                })
                .collect(Collectors.toList());

        return workTypeClassificatorMapper.toDtoList(deletedEntities);
    }
}
