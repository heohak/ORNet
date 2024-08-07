package com.demo.bait.service.LinkedDeviceServices;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.LinkedDeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.LinkedDevice;
import com.demo.bait.mapper.CommentMapper;
import com.demo.bait.mapper.LinkedDeviceMapper;
import com.demo.bait.repository.CommentRepo;
import com.demo.bait.repository.DeviceRepo;
import com.demo.bait.repository.LinkedDeviceRepo;
import com.demo.bait.service.CommentServices.CommentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class LinkedDeviceService {

    private LinkedDeviceRepo linkedDeviceRepo;
    private LinkedDeviceMapper linkedDeviceMapper;
    private DeviceRepo deviceRepo;
    private CommentService commentService;

    @Transactional
    public ResponseDTO addLinkedDevice(LinkedDeviceDTO linkedDeviceDTO) {
        LinkedDevice linkedDevice = new LinkedDevice();
        if (linkedDeviceDTO.deviceId() != null && deviceRepo.findById(linkedDeviceDTO.deviceId()).isPresent()) {
            linkedDevice.setDevice(deviceRepo.getReferenceById(linkedDeviceDTO.deviceId()));
        }
        linkedDevice.setName(linkedDeviceDTO.name());
        linkedDevice.setManufacturer(linkedDeviceDTO.manufacturer());
        linkedDevice.setProductCode(linkedDeviceDTO.productCode());
        linkedDevice.setSerialNumber(linkedDeviceDTO.serialNumber());

        if (linkedDeviceDTO.commentIds() != null) {
            Set<Comment> comments = commentService.commentIdsToCommentsSet(linkedDeviceDTO.commentIds());
            linkedDevice.setComments(comments);
        }

        if (linkedDeviceDTO.attributes() != null) {
            linkedDevice.setAttributes(linkedDeviceDTO.attributes());
        } else {
            linkedDevice.setAttributes(new HashMap<>());
        }

        linkedDeviceRepo.save(linkedDevice);
        return new ResponseDTO(linkedDevice.getId().toString());
    }

    @Transactional
    public ResponseDTO linkDevice(Integer linkedDeviceId, Integer deviceId) {
        Optional<LinkedDevice> linkedDeviceOpt = linkedDeviceRepo.findById(linkedDeviceId);
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);

        if (linkedDeviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Linked Device with id " + linkedDeviceId + " not found");
        }
        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }

        LinkedDevice linkedDevice = linkedDeviceOpt.get();
        Device device = deviceOpt.get();
        linkedDevice.setDevice(device);
        linkedDeviceRepo.save(linkedDevice);
        return new ResponseDTO("Device linked successfully");
    }

    @Transactional
    public ResponseDTO deleteLinkedDevice(Integer linkedDeviceId) {
        linkedDeviceRepo.deleteById(linkedDeviceId);
        return new ResponseDTO("Linked Device deleted");
    }

    @Transactional
    public ResponseDTO updateLinkedDevice(Integer linkedDeviceId, LinkedDeviceDTO linkedDeviceDTO) {
        Optional<LinkedDevice> linkedDeviceOpt = linkedDeviceRepo.findById(linkedDeviceId);
        if (linkedDeviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Linked Device with id " + linkedDeviceId + " not found");
        }
        LinkedDevice linkedDevice = linkedDeviceOpt.get();

        updateDeviceId(linkedDevice, linkedDeviceDTO);
        updateName(linkedDevice, linkedDeviceDTO);
        updateManufacturer(linkedDevice, linkedDeviceDTO);
        updateProductCode(linkedDevice, linkedDeviceDTO);
        updateSerialNumber(linkedDevice, linkedDeviceDTO);
        linkedDeviceRepo.save(linkedDevice);
        return new ResponseDTO("Linked Device updated successfully");
    }

    public void updateDeviceId(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.deviceId() != null) {
            Optional<Device> deviceOpt = deviceRepo.findById(linkedDeviceDTO.deviceId());
            if (deviceOpt.isEmpty()) {
                throw new EntityNotFoundException("Device with id " + linkedDeviceDTO.deviceId() + " not found");
            }
            Device device = deviceOpt.get();
            linkedDevice.setDevice(device);
        }
    }

    public void updateName(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.name() != null) {
            linkedDevice.setName(linkedDeviceDTO.name());
        }
    }

    public void updateManufacturer(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.manufacturer() != null) {
            linkedDevice.setManufacturer(linkedDeviceDTO.manufacturer());
        }
    }

    public void updateProductCode(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.productCode() != null) {
            linkedDevice.setProductCode(linkedDeviceDTO.productCode());
        }
    }

    public void updateSerialNumber(LinkedDevice linkedDevice, LinkedDeviceDTO linkedDeviceDTO) {
        if (linkedDeviceDTO.serialNumber() != null) {
            linkedDevice.setSerialNumber(linkedDeviceDTO.serialNumber());
        }
    }

    public List<LinkedDeviceDTO> getNotUsedLinkedDevices() {
        return linkedDeviceMapper.toDtoList(linkedDeviceRepo.findByDeviceId(null));
    }

    public List<LinkedDeviceDTO> getAllLinkedDevices() {
        return linkedDeviceMapper.toDtoList(linkedDeviceRepo.findAll());
    }

    public List<LinkedDeviceDTO> getLinkedDevicesByDeviceId(Integer deviceId) {
        return linkedDeviceMapper.toDtoList(linkedDeviceRepo.findByDeviceId(deviceId));
    }
}
