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
        log.info("Searching and filtering Clients with parameters - searchTerm: {}, clientTypes: {}, locationId: {}, thirdPartyId: {}, country: {}, activeCustomer: {}",
                searchTerm, clientTypes, locationId, thirdPartyId, country, activeCustomer);
        try {
            Specification<Client> combinedSpec = Specification.where(null);

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                log.debug("Adding search term filter: {}", searchTerm);
                Specification<Client> searchSpec = new ClientSpecification(searchTerm);
                combinedSpec = combinedSpec.and(searchSpec);
            }

            if (clientTypes != null && !clientTypes.isEmpty()) {
                log.debug("Adding client types filter: {}", clientTypes);
                Specification<Client> typeSpec = ClientSpecification.hasClientTypes(clientTypes);
                combinedSpec = combinedSpec.and(typeSpec);
            }

            if (locationId != null) {
                log.debug("Adding location filter with ID: {}", locationId);
                Specification<Client> locationSpec = ClientSpecification.hasLocationId(locationId);
                combinedSpec = combinedSpec.and(locationSpec);
            }

            if (thirdPartyId != null) {
                log.debug("Adding third party filter with ID: {}", thirdPartyId);
                Specification<Client> thirdPartySpec = ClientSpecification.hasThirdPartyId(thirdPartyId);
                combinedSpec = combinedSpec.and(thirdPartySpec);
            }

            if (country != null) {
                log.debug("Adding country filter: {}", country);
                Specification<Client> countrySpec = ClientSpecification.hasCountry(country);
                combinedSpec = combinedSpec.and(countrySpec);
            }

            if (activeCustomer != null) {
                log.debug("Adding active customer filter: {}", activeCustomer);
                Specification<Client> activeCustomerSpec = ClientSpecification.isActiveCustomer(activeCustomer);
                combinedSpec = combinedSpec.and(activeCustomerSpec);
            }

            List<ClientDTO> result = clientMapper.toDtoList(clientRepo.findAll(combinedSpec));
            log.info("Found {} Clients matching the criteria", result.size());
            return result;
        } catch (Exception e) {
            log.error("Error while searching and filtering Clients", e);
            throw e;
        }
    }
}
