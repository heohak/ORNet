package com.demo.bait.service.LinkedDeviceServices;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.LinkedDevice;
import com.demo.bait.repository.LinkedDeviceRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class LinkedDeviceAttributeService {

    private LinkedDeviceRepo linkedDeviceRepo;

    @Transactional
    public ResponseDTO updateLinkedDeviceAttributes(Integer linkedDeviceId, Map<String, Object> newAttributes) {
        log.info("Updating attributes for linked device with ID: {}", linkedDeviceId);
        try {
            LinkedDevice linkedDevice = linkedDeviceRepo.findById(linkedDeviceId)
                    .orElseThrow(() -> {
                        log.warn("Linked device with ID {} not found.", linkedDeviceId);
                        return new EntityNotFoundException("Linked device not found");
                    });

            log.debug("Current attributes for device ID {}: {}", linkedDeviceId, linkedDevice.getAttributes());
            log.debug("New attributes to add/update: {}", newAttributes);

            linkedDevice.getAttributes().putAll(newAttributes);
            linkedDeviceRepo.save(linkedDevice);

            log.info("Successfully updated attributes for linked device with ID: {}", linkedDeviceId);
            return new ResponseDTO("Linked device attributes updated successfully");
        } catch (Exception e) {
            log.error("Error while updating attributes for linked device with ID: {}", linkedDeviceId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO removeLinkedDeviceAttribute(Integer linkedDeviceId, String attributeName) {
        log.info("Removing attribute '{}' from linked device with ID: {}", attributeName, linkedDeviceId);
        try {
            LinkedDevice linkedDevice = linkedDeviceRepo.findById(linkedDeviceId)
                    .orElseThrow(() -> {
                        log.warn("Linked device with ID {} not found.", linkedDeviceId);
                        return new EntityNotFoundException("Linked device not found");
                    });

            log.debug("Current attributes before removal for device ID {}: {}", linkedDeviceId, linkedDevice.getAttributes());

            if (!linkedDevice.getAttributes().containsKey(attributeName)) {
                log.warn("Attribute '{}' does not exist for linked device with ID: {}", attributeName, linkedDeviceId);
            }

            linkedDevice.getAttributes().remove(attributeName);
            linkedDeviceRepo.save(linkedDevice);

            log.info("Successfully removed attribute '{}' from linked device with ID: {}", attributeName, linkedDeviceId);
            return new ResponseDTO("Attribute removed successfully");
        } catch (Exception e) {
            log.error("Error while removing attribute '{}' from linked device with ID: {}", attributeName, linkedDeviceId, e);
            throw e;
        }
    }
}
