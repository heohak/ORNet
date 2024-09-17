package com.demo.bait.service.ClientWorkerServices;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.mapper.ClientWorkerMapper;
import com.demo.bait.repository.ClientWorkerRepo;
import com.demo.bait.specification.ClientWorkerSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ClientWorkerSpecificationService {

    private ClientWorkerRepo clientWorkerRepo;
    private ClientWorkerMapper clientWorkerMapper;

    public List<ClientWorkerDTO> searchAndFilterClientWorkers(String searchTerm, Integer roleId, Integer clientId) {
        Specification<ClientWorker> combinedSpec = Specification.where(null);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Specification<ClientWorker> searchSpec = new ClientWorkerSpecification(searchTerm);
            combinedSpec = combinedSpec.and(searchSpec);
        }

        if (roleId != null) {
            Specification<ClientWorker> roleSpec = ClientWorkerSpecification.hasRoleId(roleId);
            combinedSpec = combinedSpec.and(roleSpec);
        }

        if (clientId != null) {
            Specification<ClientWorker> clientSpec = ClientWorkerSpecification.hasClientId(clientId);
            combinedSpec = combinedSpec.and(clientSpec);
        }

        return clientWorkerMapper.toDtoList(clientWorkerRepo.findAll(combinedSpec,
                Sort.by(Sort.Direction.DESC, "favorite")));
    }
}
