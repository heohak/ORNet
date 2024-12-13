package com.demo.bait.service.ClientActivityService;

import com.demo.bait.dto.ClientActivityDTO;
import com.demo.bait.dto.ClientDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.ClientActivity;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.ClientActivityMapper;
import com.demo.bait.repository.ClientActivityRepo;
import com.demo.bait.specification.ClientActivitySpecification;
import com.demo.bait.specification.ClientSpecification;
import com.demo.bait.specification.TicketSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ClientActivitySpecificationService {

    private ClientActivityMapper clientActivityMapper;
    private ClientActivityRepo clientActivityRepo;

    public List<ClientActivityDTO> searchAndFilterClientActivities(Integer statusId, Integer clientId) {
        log.info("Searching and filtering Client Activities with parameters - statusId: {}, clientId: {}", statusId, clientId);

        try {
            Specification<ClientActivity> combinedSpec = Specification.where(null);

            if (statusId != null) {
                log.debug("Adding filter for statusId: {}", statusId);
                Specification<ClientActivity> statusSpec = ClientActivitySpecification.hasStatusId(statusId);
                combinedSpec = combinedSpec.and(statusSpec);
            }

            if (clientId != null) {
                log.debug("Adding filter for clientId: {}", clientId);
                Specification<ClientActivity> clientSpec = ClientActivitySpecification.hasClientId(clientId);
                combinedSpec = combinedSpec.and(clientSpec);
            }

            List<ClientActivityDTO> result = clientActivityMapper.toDtoList(clientActivityRepo.findAll(combinedSpec));
            log.info("Found {} Client Activities matching the criteria", result.size());
            return result;
        } catch (Exception e) {
            log.error("Error while searching and filtering Client Activities", e);
            throw e;
        }
    }
}
