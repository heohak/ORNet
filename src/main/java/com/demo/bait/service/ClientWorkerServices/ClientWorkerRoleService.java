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
        Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
        Optional<ClientWorkerRoleClassificator> roleOpt = workerRoleClassificatorRepo.findById(roleId);

        if (workerOpt.isEmpty()) {
            throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
        }
        if (roleOpt.isEmpty()) {
            throw new EntityNotFoundException("Client worker role classificator with id " + roleId + " not found");
        }

        ClientWorker worker = workerOpt.get();
        ClientWorkerRoleClassificator role = roleOpt.get();
        worker.getRoles().add(role);
        clientWorkerRepo.save(worker);
        return new ResponseDTO("Client worker role added successfully to worker");
    }

    @Transactional
    public ResponseDTO addRolesToWorker(Integer workerId, ClientWorkerDTO clientWorkerDTO) {
        Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);

        if (workerOpt.isEmpty()) {
            throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
        }

        ClientWorker worker = workerOpt.get();
        if (clientWorkerDTO.roleIds() != null) {
            Set<ClientWorkerRoleClassificator> roles = workerRoleClassificatorService
                    .roleIdsToRolesSet(clientWorkerDTO.roleIds());
            worker.setRoles(roles);
        }
        clientWorkerRepo.save(worker);
        return new ResponseDTO("Roles added successfully to worker");
    }

    public List<ClientWorkerRoleClassificatorDTO> getWorkerRoles(Integer workerId) {
        Optional<ClientWorker> workerOpt = clientWorkerRepo.findById(workerId);
        if (workerOpt.isEmpty()) {
            throw new EntityNotFoundException("ClientWorker with id " + workerId + " not found");
        }
        ClientWorker worker = workerOpt.get();
        return workerRoleClassificatorMapper.toDtoList(worker.getRoles().stream().toList());
    }
}
