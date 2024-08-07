package com.demo.bait.service.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.ClientWorkerRoleClassificatorDTO;
import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import com.demo.bait.mapper.classificator.ClientWorkerRoleClassificatorMapper;
import com.demo.bait.repository.classificator.ClientWorkerRoleClassificatorRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ClientWorkerRoleClassificatorService {

    private ClientWorkerRoleClassificatorRepo workerRoleClassificatorRepo;
    private ClientWorkerRoleClassificatorMapper workerRoleClassificatorMapper;

    @Transactional
    public ResponseDTO addWorkerRoleClassificator(ClientWorkerRoleClassificatorDTO workerRoleClassificatorDTO) {
        ClientWorkerRoleClassificator workerRoleClassificator = new ClientWorkerRoleClassificator();
        workerRoleClassificator.setRole(workerRoleClassificatorDTO.role());
        workerRoleClassificatorRepo.save(workerRoleClassificator);
        return new ResponseDTO("Worker role classificator added successfully");
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
}
