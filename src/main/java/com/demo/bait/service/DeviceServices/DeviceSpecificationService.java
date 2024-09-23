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
        Specification<Device> combinedSpec = Specification.where(null);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Specification<Device> searchSpec = new DeviceSpecification(searchTerm);
            combinedSpec = combinedSpec.and(searchSpec);
        }

        if (classificatorId != null) {
            Specification<Device> classificatorSpec = DeviceSpecification.hasClassificatorId(classificatorId);
            combinedSpec = combinedSpec.and(classificatorSpec);
        }

        if (clientId != null) {
            Specification<Device> clientSpec = DeviceSpecification.hasClientId(clientId);
            combinedSpec = combinedSpec.and(clientSpec);
        }

        if (locationId != null) {
            Specification<Device> locationSpec = DeviceSpecification.hasLocationId(locationId);
            combinedSpec = combinedSpec.and(locationSpec);
        }

        if (writtenOff != null) {
            Specification<Device> writtenOffSpec = DeviceSpecification.isWrittenOff(writtenOff);
            combinedSpec = combinedSpec.and(writtenOffSpec);
        }

        return deviceMapper.toDtoList(deviceRepo.findAll(combinedSpec));
    }
}
