package com.demo.bait.service.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.ClientWorkerRoleClassificatorDTO;
import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import com.demo.bait.mapper.classificator.ClientWorkerRoleClassificatorMapper;
import com.demo.bait.repository.classificator.ClientWorkerRoleClassificatorRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ClientWorkerRoleClassificatorService {

    private ClientWorkerRoleClassificatorRepo workerRoleClassificatorRepo;
    private ClientWorkerRoleClassificatorMapper workerRoleClassificatorMapper;


    public ResponseDTO addWorkerRoleClassificator(ClientWorkerRoleClassificatorDTO workerRoleClassificatorDTO) {
        ClientWorkerRoleClassificator workerRoleClassificator = new ClientWorkerRoleClassificator();
        workerRoleClassificator.setRole(workerRoleClassificatorDTO.role());
        workerRoleClassificatorRepo.save(workerRoleClassificator);
        return new ResponseDTO("Worker role classificator added successfully");
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
