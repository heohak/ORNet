package com.demo.bait.service.ClientServices;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.mapper.ClientMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.specification.ClientSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ClientSpecificationService {

    private ClientRepo clientRepo;
    private ClientMapper clientMapper;

    public List<ClientDTO> searchAndFilterClients(String searchTerm, String clientType, Integer locationId,
                                                  Integer thirdPartyId) {
        Specification<Client> combinedSpec = Specification.where(null);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Specification<Client> searchSpec = new ClientSpecification(searchTerm);
            combinedSpec = combinedSpec.and(searchSpec);
        }

        if (clientType != null && !clientType.trim().isEmpty()) {
            Specification<Client> typeSpec = ClientSpecification.hasClientType(clientType);
            combinedSpec = combinedSpec.and(typeSpec);
        }

        if (locationId != null) {
            Specification<Client> locationSpec = ClientSpecification.hasLocationId(locationId);
            combinedSpec = combinedSpec.and(locationSpec);
        }

        if (thirdPartyId != null) {
            Specification<Client> thirdPartySpec = ClientSpecification.hasThirdPartyId(thirdPartyId);
            combinedSpec = combinedSpec.and(thirdPartySpec);
        }

        return clientMapper.toDtoList(clientRepo.findAll(combinedSpec));
    }
}
