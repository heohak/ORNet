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
    public ClientWorkerRoleClassificatorDTO addWorkerRoleClassificator(ClientWorkerRoleClassificatorDTO workerRoleClassificatorDTO) {
        ClientWorkerRoleClassificator workerRoleClassificator = new ClientWorkerRoleClassificator();
        workerRoleClassificator.setRole(workerRoleClassificatorDTO.role());
        workerRoleClassificatorRepo.save(workerRoleClassificator);
        return workerRoleClassificatorMapper.toDto(workerRoleClassificator);
    }

    @Transactional
    public ResponseDTO updateWorkerRoleClassificator(Integer roleId, ClientWorkerRoleClassificatorDTO roleDTO) {
        Optional<ClientWorkerRoleClassificator> roleOpt = workerRoleClassificatorRepo.findById(roleId);
        if (roleOpt.isEmpty()) {
            throw new EntityNotFoundException("Client worker role classificator with id " + roleId + " not found");
        }
        ClientWorkerRoleClassificator role = roleOpt.get();

        if (roleDTO.role() != null) {
            role.setRole(roleDTO.role());
        }
        workerRoleClassificatorRepo.save(role);
        return new ResponseDTO("Client worker role classificator updated successfully");
    }

    @Transactional
    public ResponseDTO deleteWorkerRoleClassificator(Integer roleId) {
        Optional<ClientWorkerRoleClassificator> roleClassificatorOpt = workerRoleClassificatorRepo.findById(roleId);
        if (roleClassificatorOpt.isEmpty()) {
            throw new EntityNotFoundException("Worker role classificator with id " + roleId + " not found");
        }
        ClientWorkerRoleClassificator roleClassificator = roleClassificatorOpt.get();

        List<ClientWorker> associatedWorkers = clientWorkerRepo.findByRolesContaining(roleClassificator);
        for (ClientWorker worker : associatedWorkers) {
            worker.getRoles().remove(roleClassificator);
            clientWorkerRepo.save(worker);
        }

        workerRoleClassificatorRepo.delete(roleClassificator);
        return new ResponseDTO("Worker role classificator deleted successfully");
    }

    public List<ClientWorkerRoleClassificatorDTO> getAllWorkerRoleClassificators() {
        return workerRoleClassificatorMapper.toDtoList(workerRoleClassificatorRepo.findAll());
    }

    public Set<ClientWorkerRoleClassificator> roleIdsToRolesSet(List<Integer> roleIds) {
        Set<ClientWorkerRoleClassificator> roles = new HashSet<>();
        for (Integer roleId : roleIds) {
            ClientWorkerRoleClassificator role = workerRoleClassificatorRepo.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid client worker role ID " + roleId));
            roles.add(role);
        }
        return roles;
    }

    public List<ClientWorkerRoleClassificatorDTO> getWorkerRoleClassificatorHistory(Integer roleId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(ClientWorkerRoleClassificator.class, roleId);

        List<ClientWorkerRoleClassificator> history = new ArrayList<>();
        for (Number rev : revisions) {
            ClientWorkerRoleClassificator clientWorkerRoleClassificatorVersion = auditReader
                    .find(ClientWorkerRoleClassificator.class, roleId, rev);
            history.add(clientWorkerRoleClassificatorVersion);
        }
        return workerRoleClassificatorMapper.toDtoList(history);
    }

    public List<ClientWorkerRoleClassificatorDTO> getDeletedRoles() {
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

        return workerRoleClassificatorMapper.toDtoList(deletedEntities);
    }
}
