package com.demo.bait.service.ClientWorkerServices;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.ClientWorkerRoleClassificatorDTO;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import com.demo.bait.mapper.classificator.ClientWorkerRoleClassificatorMapper;
import com.demo.bait.repository.ClientWorkerRepo;
import com.demo.bait.repository.classificator.ClientWorkerRoleClassificatorRepo;
import com.demo.bait.service.classificator.ClientWorkerRoleClassificatorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ClientWorkerRoleService {

    private ClientWorkerRepo clientWorkerRepo;
    private ClientWorkerRoleClassificatorRepo workerRoleClassificatorRepo;
    private ClientWorkerRoleClassificatorMapper workerRoleClassificatorMapper;
    private ClientWorkerRoleClassificatorService workerRoleClassificatorService;

    @Transactional
    public ResponseDTO addRoleToEmployee(Integer workerId, Integer roleId) {
        log.info("Adding role with ID: {} to worker with ID: {}", roleId, workerId);
        try {
            Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
            Optional<ClientWorkerRoleClassificator> roleOpt = workerRoleClassificatorRepo.findById(roleId);

            if (workerOpt.isEmpty()) {
                log.warn("Worker with ID {} not found", workerId);
                throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
            }
            if (roleOpt.isEmpty()) {
                log.warn("Role with ID {} not found", roleId);
                throw new EntityNotFoundException("Client worker role classificator with id " + roleId + " not found");
            }

            ClientWorker worker = workerOpt.get();
            ClientWorkerRoleClassificator role = roleOpt.get();

            log.debug("Adding role with ID: {} to worker with ID: {}", roleId, workerId);
            worker.getRoles().add(role);
            clientWorkerRepo.save(worker);

            log.info("Successfully added role with ID: {} to worker with ID: {}", roleId, workerId);
            return new ResponseDTO("Client worker role added successfully to worker");
        } catch (Exception e) {
            log.error("Error while adding role with ID: {} to worker with ID: {}", roleId, workerId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO addRolesToWorker(Integer workerId, ClientWorkerDTO clientWorkerDTO) {
        log.info("Adding multiple roles to worker with ID: {}", workerId);
        try {
            Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);

            if (workerOpt.isEmpty()) {
                log.warn("Worker with ID {} not found", workerId);
                throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
            }

            ClientWorker worker = workerOpt.get();
            if (clientWorkerDTO.roleIds() != null) {
                log.debug("Adding roles with IDs: {} to worker with ID: {}", clientWorkerDTO.roleIds(), workerId);
                Set<ClientWorkerRoleClassificator> roles = workerRoleClassificatorService
                        .roleIdsToRolesSet(clientWorkerDTO.roleIds());
                worker.setRoles(roles);
            }
            clientWorkerRepo.save(worker);
            log.info("Successfully added multiple roles to worker with ID: {}", workerId);
            return new ResponseDTO("Roles added successfully to worker");
        } catch (Exception e) {
            log.error("Error while adding roles to worker with ID: {}", workerId, e);
            throw e;
        }
    }

    public List<ClientWorkerRoleClassificatorDTO> getWorkerRoles(Integer workerId) {
        log.info("Fetching roles for worker with ID: {}", workerId);
        try {
            Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
            if (workerOpt.isEmpty()) {
                log.warn("Worker with ID {} not found", workerId);
                throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
            }

            ClientWorker worker = workerOpt.get();
            List<ClientWorkerRoleClassificatorDTO> roles = workerRoleClassificatorMapper.toDtoList(worker.getRoles().stream().toList());

            log.debug("Fetched {} roles for worker with ID: {}", roles.size(), workerId);
            return roles;
        } catch (Exception e) {
            log.error("Error while fetching roles for worker with ID: {}", workerId, e);
            throw e;
        }
    }
}
