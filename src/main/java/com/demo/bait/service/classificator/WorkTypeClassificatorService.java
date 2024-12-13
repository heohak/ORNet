package com.demo.bait.service.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.WorkTypeClassificatorDTO;
import com.demo.bait.entity.ClientActivity;
import com.demo.bait.entity.Ticket;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.classificator.WorkTypeClassificatorMapper;
import com.demo.bait.repository.ClientActivityRepo;
import com.demo.bait.repository.TicketRepo;
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
    private TicketRepo ticketRepo;
    private ClientActivityRepo clientActivityRepo;

    @Transactional
    public WorkTypeClassificatorDTO addWorkTypeClassificator(WorkTypeClassificatorDTO workTypeClassificatorDTO) {
        log.info("Adding new Work Type Classificator: {}", workTypeClassificatorDTO);
        try {
            WorkTypeClassificator workTypeClassificator = new WorkTypeClassificator();
            workTypeClassificator.setWorkType(workTypeClassificatorDTO.workType());
            workTypeClassificatorRepo.save(workTypeClassificator);
            log.info("Successfully added Work Type Classificator: {}", workTypeClassificator);
            return workTypeClassificatorMapper.toDto(workTypeClassificator);
        } catch (Exception e) {
            log.error("Error while adding Work Type Classificator: {}", workTypeClassificatorDTO, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO updateWorkTypeClassificator(Integer workTypeId, WorkTypeClassificatorDTO workTypeClassificatorDTO) {
        log.info("Updating Work Type Classificator with ID: {}", workTypeId);
        try {
            Optional<WorkTypeClassificator> workTypeOpt = workTypeClassificatorRepo.findById(workTypeId);
            if (workTypeOpt.isEmpty()) {
                log.warn("Work Type Classificator with ID {} not found", workTypeId);
                throw new EntityNotFoundException("Work Type classificator with id " + workTypeId + " not found");
            }
            WorkTypeClassificator workType = workTypeOpt.get();

            if (workTypeClassificatorDTO.workType() != null) {
                workType.setWorkType(workTypeClassificatorDTO.workType());
            }
            workTypeClassificatorRepo.save(workType);
            log.info("Successfully updated Work Type Classificator with ID: {}", workTypeId);
            return new ResponseDTO("Work type classificator updated successfully");
        } catch (Exception e) {
            log.error("Error while updating Work Type Classificator with ID: {}", workTypeId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO deleteWorkTypeClassificator(Integer workTypeId) {
        log.info("Deleting Work Type Classificator with ID: {}", workTypeId);
        try {
            Optional<WorkTypeClassificator> workTypeOpt = workTypeClassificatorRepo.findById(workTypeId);
            if (workTypeOpt.isEmpty()) {
                log.warn("Work Type Classificator with ID {} not found", workTypeId);
                throw new EntityNotFoundException("Work type classificator with id " + workTypeId + " not found");
            }
            WorkTypeClassificator workType = workTypeOpt.get();

            log.debug("Unlinking associated Client Activities for Work Type ID: {}", workTypeId);
            List<ClientActivity> clientActivities = clientActivityRepo.findByWorkTypesContaining(workType);
            for (ClientActivity clientActivity : clientActivities) {
                clientActivity.getWorkTypes().remove(workType);
                clientActivityRepo.save(clientActivity);
            }

            log.debug("Unlinking associated Tickets for Work Type ID: {}", workTypeId);
            List<Ticket> tickets = ticketRepo.findByWorkTypesContaining(workType);
            for (Ticket ticket : tickets) {
                ticket.getWorkTypes().remove(workType);
                ticketRepo.save(ticket);
            }

            workTypeClassificatorRepo.delete(workType);
            log.info("Successfully deleted Work Type Classificator with ID: {}", workTypeId);
            return new ResponseDTO("Work type deleted successfully");
        } catch (Exception e) {
            log.error("Error while deleting Work Type Classificator with ID: {}", workTypeId, e);
            throw e;
        }
    }

    public List<WorkTypeClassificatorDTO> getAllWorkTypes() {
        log.info("Fetching all Work Type Classificators");
        try {
            List<WorkTypeClassificatorDTO> result = workTypeClassificatorMapper.toDtoList(workTypeClassificatorRepo.findAll());
            log.debug("Fetched Work Type Classificators: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error while fetching all Work Type Classificators", e);
            throw e;
        }
    }

    public Set<WorkTypeClassificator> workTypeIdsToWorkTypesSet(List<Integer> workTypeIds) {
        log.info("Converting Work Type IDs to Work Type Set: {}", workTypeIds);
        try {
            Set<WorkTypeClassificator> workTypes = new HashSet<>();
            for (Integer id : workTypeIds) {
                WorkTypeClassificator workType = workTypeClassificatorRepo.findById(id)
                        .orElseThrow(() -> {
                            log.warn("Invalid Work Type ID: {}", id);
                            return new IllegalArgumentException("Invalid work type classificator ID: " + id);
                        });
                workTypes.add(workType);
            }
            log.debug("Converted Work Type Set: {}", workTypes);
            return workTypes;
        } catch (Exception e) {
            log.error("Error while converting Work Type IDs to Work Type Set: {}", workTypeIds, e);
            throw e;
        }
    }

    public List<WorkTypeClassificatorDTO> getWorkTypeHistory(Integer workTypeId) {
        log.info("Fetching history for Work Type Classificator with ID: {}", workTypeId);
        try {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            List<Number> revisions = auditReader.getRevisions(WorkTypeClassificator.class, workTypeId);

            List<WorkTypeClassificator> history = new ArrayList<>();
            for (Number rev : revisions) {
                WorkTypeClassificator workTypeClassificatorVersion = auditReader
                        .find(WorkTypeClassificator.class, workTypeId, rev);
                history.add(workTypeClassificatorVersion);
            }
            List<WorkTypeClassificatorDTO> result = workTypeClassificatorMapper.toDtoList(history);
            log.debug("Fetched history for Work Type Classificator with ID {}: {}", workTypeId, result);
            return result;
        } catch (Exception e) {
            log.error("Error while fetching history for Work Type Classificator with ID: {}", workTypeId, e);
            throw e;
        }
    }

    public List<WorkTypeClassificatorDTO> getDeletedWorkTypeClassificators() {
        log.info("Fetching deleted Work Type Classificators");
        try {
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
                                revisionEntity.getId() - 1
                        );

                        return lastStateBeforeDeletion != null ? lastStateBeforeDeletion : deletedEntity;
                    })
                    .collect(Collectors.toList());

            List<WorkTypeClassificatorDTO> deletedDTOs = workTypeClassificatorMapper.toDtoList(deletedEntities);
            log.debug("Fetched deleted Work Type Classificators: {}", deletedDTOs);
            return deletedDTOs;
        } catch (Exception e) {
            log.error("Error while fetching deleted Work Type Classificators", e);
            throw e;
        }
    }
}
