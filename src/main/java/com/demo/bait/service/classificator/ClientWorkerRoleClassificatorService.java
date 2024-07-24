package com.demo.bait.service.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.ClientWorkerRoleClassificatorDTO;
import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import com.demo.bait.mapper.classificator.ClientWorkerRoleClassificatorMapper;
import com.demo.bait.repository.classificator.ClientWorkerRoleClassificatorRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
