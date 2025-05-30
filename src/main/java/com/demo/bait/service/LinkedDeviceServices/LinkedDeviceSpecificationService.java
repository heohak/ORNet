package com.demo.bait.service.LinkedDeviceServices;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.LinkedDeviceDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.LinkedDevice;
import com.demo.bait.mapper.LinkedDeviceMapper;
import com.demo.bait.repository.LinkedDeviceRepo;
import com.demo.bait.specification.DeviceSpecification;
import com.demo.bait.specification.LinkedDeviceSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LinkedDeviceSpecificationService {

    private LinkedDeviceRepo linkedDeviceRepo;
    private LinkedDeviceMapper linkedDeviceMapper;

    public List<LinkedDeviceDTO> searchAndFilterLinkedDevices(String searchTerm, Integer locationId, Integer deviceId,
                                                              Boolean template, LocalDate date, String comparison) {
        log.info("Searching and filtering linked devices with parameters - " +
                        "searchTerm: {}, locationId: {}, deviceId: {}, template: {}",
                searchTerm, locationId, deviceId, template);

        try {
            Specification<LinkedDevice> combinedSpec = Specification.where(null);

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                log.debug("Adding search term filter: {}", searchTerm);
                Specification<LinkedDevice> searchSpec = new LinkedDeviceSpecification(searchTerm);
                combinedSpec = combinedSpec.and(searchSpec);
            }

            if (locationId != null) {
                log.debug("Adding location filter with ID: {}", locationId);
                Specification<LinkedDevice> locationSpec = LinkedDeviceSpecification.hasLocationId(locationId);
                combinedSpec = combinedSpec.and(locationSpec);
            }

            if (deviceId != null) {
                log.debug("Adding device filter with ID: {}", deviceId);
                Specification<LinkedDevice> deviceSpec = LinkedDeviceSpecification.hasDeviceId(deviceId);
                combinedSpec = combinedSpec.and(deviceSpec);
            }

            if (template != null) {
                log.debug("Adding template filter with value: {}", template);
                Specification<LinkedDevice> templateSpec = LinkedDeviceSpecification.isTemplate(template);
                combinedSpec = combinedSpec.and(templateSpec);
            }

            if (date != null && comparison != null && !comparison.trim().isEmpty()) {
                log.debug("Adding filter date: {} and comparison: {}", date, comparison);
                Specification<LinkedDevice> dateSpec = LinkedDeviceSpecification.hasIntroducedDate(date, comparison);
                combinedSpec = combinedSpec.and(dateSpec);
            }

            List<LinkedDeviceDTO> linkedDevices = linkedDeviceMapper.toDtoList(linkedDeviceRepo.findAll(combinedSpec));
            log.info("Found {} linked devices matching the criteria.", linkedDevices.size());
            return linkedDevices;
        } catch (Exception e) {
            log.error("Error while searching and filtering linked devices.", e);
            throw e;
        }
    }

    public List<LinkedDeviceDTO> getNotUsedLinkedDevices() {
        log.info("Fetching all linked devices not associated with any device and with template set to false or null");

        List<LinkedDevice> linkedDevices = linkedDeviceRepo.findAll(LinkedDeviceSpecification.isNotUsed());
        List<LinkedDeviceDTO> devices = linkedDeviceMapper.toDtoList(linkedDevices);

        log.info("Fetched {} linked devices not associated with any device and with template set to false or null", devices.size());
        return devices;
    }
}
