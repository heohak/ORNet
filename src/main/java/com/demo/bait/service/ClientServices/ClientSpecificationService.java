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

    public List<ClientDTO> searchAndFilterClients(String searchTerm, List<String> clientTypes, Integer locationId,
                                                  Integer thirdPartyId, String country, Boolean activeCustomer) {
        Specification<Client> combinedSpec = Specification.where(null);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Specification<Client> searchSpec = new ClientSpecification(searchTerm);
            combinedSpec = combinedSpec.and(searchSpec);
        }

        if (clientTypes != null && clientTypes.size() > 0) {
            Specification<Client> typeSpec = ClientSpecification.hasClientTypes(clientTypes);
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

        if (country != null) {
            Specification<Client> countrySpec = ClientSpecification.hasCountry(country);
            combinedSpec = combinedSpec.and(countrySpec);
        }

        if (activeCustomer != null) {
            Specification<Client> activeCustomerSpec = ClientSpecification.isActiveCustomer(activeCustomer);
            combinedSpec = combinedSpec.and(activeCustomerSpec);
        }

        return clientMapper.toDtoList(clientRepo.findAll(combinedSpec));
    }
}
