package com.demo.bait.service.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.ClientWorkerRoleClassificatorDTO;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.classificator.ClientWorkerRoleClassificatorMapper;
import com.demo.bait.repository.ClientWorkerRepo;
import com.demo.bait.repository.classificator.ClientWorkerRoleClassificatorRepo;
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

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ClientWorkerRoleClassificatorService {

    private ClientWorkerRoleClassificatorRepo workerRoleClassificatorRepo;
    private ClientWorkerRoleClassificatorMapper workerRoleClassificatorMapper;
    private EntityManager entityManager;
    private ClientWorkerRepo clientWorkerRepo;

    @Transactional
    public ClientWorkerRoleClassificatorDTO addWorkerRoleClassificator(
            ClientWorkerRoleClassificatorDTO workerRoleClassificatorDTO) {
        log.info("Adding new Worker Role Classificator: {}", workerRoleClassificatorDTO);
        try {
            ClientWorkerRoleClassificator workerRoleClassificator = new ClientWorkerRoleClassificator();
            workerRoleClassificator.setRole(workerRoleClassificatorDTO.role());
            workerRoleClassificatorRepo.save(workerRoleClassificator);
            log.info("Successfully added Worker Role Classificator: {}", workerRoleClassificator);
            return workerRoleClassificatorMapper.toDto(workerRoleClassificator);
        } catch (Exception e) {
            log.error("Error while adding Worker Role Classificator: {}", workerRoleClassificatorDTO, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO updateWorkerRoleClassificator(Integer roleId, ClientWorkerRoleClassificatorDTO roleDTO) {
        log.info("Updating Worker Role Classificator with ID: {}", roleId);
        try {
            Optional<ClientWorkerRoleClassificator> roleOpt = workerRoleClassificatorRepo.findById(roleId);
            if (roleOpt.isEmpty()) {
                log.warn("Worker Role Classificator with ID {} not found", roleId);
                throw new EntityNotFoundException("Client worker role classificator with id " + roleId + " not found");
            }
            ClientWorkerRoleClassificator role = roleOpt.get();

            if (roleDTO.role() != null) {
                role.setRole(roleDTO.role());
            }
            workerRoleClassificatorRepo.save(role);
            log.info("Successfully updated Worker Role Classificator with ID: {}", roleId);
            return new ResponseDTO("Client worker role classificator updated successfully");
        } catch (Exception e) {
            log.error("Error while updating Worker Role Classificator with ID: {}", roleId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO deleteWorkerRoleClassificator(Integer roleId) {
        log.info("Deleting Worker Role Classificator with ID: {}", roleId);
        try {
            Optional<ClientWorkerRoleClassificator> roleClassificatorOpt = workerRoleClassificatorRepo.findById(roleId);
            if (roleClassificatorOpt.isEmpty()) {
                log.warn("Worker Role Classificator with ID {} not found", roleId);
                throw new EntityNotFoundException("Worker role classificator with id " + roleId + " not found");
            }
            ClientWorkerRoleClassificator roleClassificator = roleClassificatorOpt.get();

            log.debug("Unlinking associated Client Workers for Role ID: {}", roleId);
            List<ClientWorker> associatedWorkers = clientWorkerRepo.findByRolesContaining(roleClassificator);
            for (ClientWorker worker : associatedWorkers) {
                worker.getRoles().remove(roleClassificator);
                clientWorkerRepo.save(worker);
            }

            workerRoleClassificatorRepo.delete(roleClassificator);
            log.info("Successfully deleted Worker Role Classificator with ID: {}", roleId);
            return new ResponseDTO("Worker role classificator deleted successfully");
        } catch (Exception e) {
            log.error("Error while deleting Worker Role Classificator with ID: {}", roleId, e);
            throw e;
        }
    }

    public List<ClientWorkerRoleClassificatorDTO> getAllWorkerRoleClassificators() {
        log.info("Fetching all Worker Role Classificators");
        List<ClientWorkerRoleClassificatorDTO> result = workerRoleClassificatorMapper
                .toDtoList(workerRoleClassificatorRepo.findAll());
        log.debug("Fetched Worker Role Classificators: {}", result);
        return result;
    }

    public Set<ClientWorkerRoleClassificator> roleIdsToRolesSet(List<Integer> roleIds) {
        log.info("Converting Role IDs to Role Set: {}", roleIds);
        try {
            Set<ClientWorkerRoleClassificator> roles = new HashSet<>();
            for (Integer roleId : roleIds) {
                ClientWorkerRoleClassificator role = workerRoleClassificatorRepo.findById(roleId)
                        .orElseThrow(() -> {
                            log.warn("Invalid Role ID: {}", roleId);
                            return new IllegalArgumentException("Invalid client worker role ID " + roleId);
                        });
                roles.add(role);
            }
            log.debug("Converted Role Set: {}", roles);
            return roles;
        } catch (Exception e) {
            log.error("Error while converting Role IDs to Role Set: {}", roleIds, e);
            throw e;
        }
    }

    public List<ClientWorkerRoleClassificatorDTO> getWorkerRoleClassificatorHistory(Integer roleId) {
        log.info("Fetching history for Worker Role Classificator with ID: {}", roleId);
        try {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            List<Number> revisions = auditReader.getRevisions(ClientWorkerRoleClassificator.class, roleId);

            List<ClientWorkerRoleClassificator> history = new ArrayList<>();
            for (Number rev : revisions) {
                ClientWorkerRoleClassificator version = auditReader.find(ClientWorkerRoleClassificator.class, roleId, rev);
                history.add(version);
            }
            List<ClientWorkerRoleClassificatorDTO> result = workerRoleClassificatorMapper.toDtoList(history);
            log.debug("Fetched history for Worker Role Classificator with ID {}: {}", roleId, result);
            return result;
        } catch (Exception e) {
            log.error("Error while fetching history for Worker Role Classificator with ID: {}", roleId, e);
            throw e;
        }
    }

    public List<ClientWorkerRoleClassificatorDTO> getDeletedRoles() {
        log.info("Fetching deleted Worker Role Classificators");
        try {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);

            AuditQuery query = auditReader.createQuery()
                    .forRevisionsOfEntity(ClientWorkerRoleClassificator.class, false, true)
                    .add(AuditEntity.revisionType().eq(org.hibernate.envers.RevisionType.DEL));

            List<Object[]> result = query.getResultList();

            List<ClientWorkerRoleClassificator> deletedEntities = result.stream()
                    .map(r -> {
                        ClientWorkerRoleClassificator deletedEntity = (ClientWorkerRoleClassificator) r[0];
                        DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) r[1];

                        ClientWorkerRoleClassificator lastStateBeforeDeletion = auditReader.find(
                                ClientWorkerRoleClassificator.class,
                                deletedEntity.getId(),
                                revisionEntity.getId() - 1
                        );

                        return lastStateBeforeDeletion != null ? lastStateBeforeDeletion : deletedEntity;
                    })
                    .collect(Collectors.toList());

            List<ClientWorkerRoleClassificatorDTO> deletedDTOs = workerRoleClassificatorMapper.toDtoList(deletedEntities);
            log.debug("Fetched deleted Worker Role Classificators: {}", deletedDTOs);
            return deletedDTOs;
        } catch (Exception e) {
            log.error("Error while fetching deleted Worker Role Classificators", e);
            throw e;
        }
    }
}
