package com.demo.bait.service.DeviceServices;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.mapper.DeviceMapper;
import com.demo.bait.repository.DeviceRepo;
import com.demo.bait.specification.DeviceSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceSpecificationService {

    private DeviceRepo deviceRepo;
    private DeviceMapper deviceMapper;

    public List<DeviceDTO> searchAndFilterDevices(String searchTerm, Integer classificatorId, Integer clientId,
                                                  Integer locationId, Boolean writtenOff) {
        log.info("Searching and filtering devices with parameters - " +
                        "searchTerm: {}, classificatorId: {}, clientId: {}, locationId: {}, writtenOff: {}",
                searchTerm, classificatorId, clientId, locationId, writtenOff);

        try {
            Specification<Device> combinedSpec = Specification.where(null);

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                log.debug("Adding search term filter: {}", searchTerm);
                Specification<Device> searchSpec = new DeviceSpecification(searchTerm);
                combinedSpec = combinedSpec.and(searchSpec);
            }

            if (classificatorId != null) {
                log.debug("Adding classificator filter with ID: {}", classificatorId);
                Specification<Device> classificatorSpec = DeviceSpecification.hasClassificatorId(classificatorId);
                combinedSpec = combinedSpec.and(classificatorSpec);
            }

            if (clientId != null) {
                log.debug("Adding client filter with ID: {}", clientId);
                Specification<Device> clientSpec = DeviceSpecification.hasClientId(clientId);
                combinedSpec = combinedSpec.and(clientSpec);
            }

            if (locationId != null) {
                log.debug("Adding location filter with ID: {}", locationId);
                Specification<Device> locationSpec = DeviceSpecification.hasLocationId(locationId);
                combinedSpec = combinedSpec.and(locationSpec);
            }

            if (writtenOff != null) {
                log.debug("Adding written-off filter with value: {}", writtenOff);
                Specification<Device> writtenOffSpec = DeviceSpecification.isWrittenOff(writtenOff);
                combinedSpec = combinedSpec.and(writtenOffSpec);
            }

            List<DeviceDTO> devices = deviceMapper.toDtoList(deviceRepo.findAll(combinedSpec));
            log.info("Found {} devices matching the criteria.", devices.size());
            return devices;
        } catch (Exception e) {
            log.error("Error while searching and filtering devices.", e);
            throw e;
        }
    }
}
