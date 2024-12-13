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

    public List<ClientWorkerDTO> searchAndFilterClientWorkers(String searchTerm, Integer roleId, Integer clientId,
                                                              Boolean favorite, Integer locationId, String country) {
        log.info("Starting search and filter for Client Workers with parameters - " +
                        "searchTerm: {}, roleId: {}, clientId: {}, favorite: {}, locationId: {}, country: {}",
                searchTerm, roleId, clientId, favorite, locationId, country);

        try {
            Specification<ClientWorker> combinedSpec = Specification.where(null);

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                log.debug("Adding search term filter: {}", searchTerm);
                Specification<ClientWorker> searchSpec = new ClientWorkerSpecification(searchTerm);
                combinedSpec = combinedSpec.and(searchSpec);
            }

            if (roleId != null) {
                log.debug("Adding role filter with roleId: {}", roleId);
                Specification<ClientWorker> roleSpec = ClientWorkerSpecification.hasRoleId(roleId);
                combinedSpec = combinedSpec.and(roleSpec);
            }

            if (clientId != null) {
                log.debug("Adding client filter with clientId: {}", clientId);
                Specification<ClientWorker> clientSpec = ClientWorkerSpecification.hasClientId(clientId);
                combinedSpec = combinedSpec.and(clientSpec);
            }

            if (favorite != null) {
                log.debug("Adding favorite filter with value: {}", favorite);
                Specification<ClientWorker> favoriteSpec = ClientWorkerSpecification.isFavorite();
                combinedSpec = combinedSpec.and(favoriteSpec);
            }

            if (locationId != null) {
                log.debug("Adding location filter with locationId: {}", locationId);
                Specification<ClientWorker> locationSpec = ClientWorkerSpecification.hasLocationId(locationId);
                combinedSpec = combinedSpec.and(locationSpec);
            }

            if (country != null) {
                log.debug("Adding country filter: {}", country);
                Specification<ClientWorker> countrySpec = ClientWorkerSpecification.hasLocationCountry(country);
                combinedSpec = combinedSpec.and(countrySpec);
            }

            List<ClientWorkerDTO> result = clientWorkerMapper.toDtoList(
                    clientWorkerRepo.findAll(combinedSpec, Sort.by(Sort.Direction.DESC, "favorite"))
            );

            log.info("Found {} Client Workers matching the criteria.", result.size());
            return result;
        } catch (Exception e) {
            log.error("Error occurred while searching and filtering Client Workers.", e);
            throw e;
        }
    }
}
