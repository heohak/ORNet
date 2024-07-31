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
        LinkedDevice linkedDevice = linkedDeviceRepo.findById(linkedDeviceId)
                .orElseThrow(() -> new EntityNotFoundException("Linked device not found"));
        linkedDevice.getAttributes().putAll(newAttributes);
        linkedDeviceRepo.save(linkedDevice);
        return new ResponseDTO("Linked device attributes updated successfully");
    }

    @Transactional
    public ResponseDTO removeLinkedDeviceAttribute(Integer linkedDeviceId, String attributeName) {
        LinkedDevice linkedDevice = linkedDeviceRepo.findById(linkedDeviceId)
                .orElseThrow(() -> new EntityNotFoundException("Linked device not found"));
        linkedDevice.getAttributes().remove(attributeName);
        linkedDeviceRepo.save(linkedDevice);
        return new ResponseDTO("Attribute removed successfully");
    }
}
