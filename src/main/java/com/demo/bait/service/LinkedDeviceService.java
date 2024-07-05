package com.demo.bait.service;

import com.demo.bait.dto.LinkedDeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.LinkedDevice;
import com.demo.bait.mapper.LinkedDeviceMapper;
import com.demo.bait.repository.DeviceRepo;
import com.demo.bait.repository.LinkedDeviceRepo;
import com.sun.jdi.IntegerValue;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class LinkedDeviceService {

    private LinkedDeviceRepo linkedDeviceRepo;
    private LinkedDeviceMapper linkedDeviceMapper;
    private DeviceRepo deviceRepo;

    public ResponseDTO addLinkedDevice(LinkedDeviceDTO linkedDeviceDTO) {
        LinkedDevice linkedDevice = new LinkedDevice();
        if (linkedDeviceDTO.deviceId() != null && deviceRepo.findById(linkedDeviceDTO.deviceId()).isPresent()) {
            linkedDevice.setDevice(deviceRepo.getReferenceById(linkedDeviceDTO.deviceId()));
        }
        linkedDevice.setName(linkedDeviceDTO.name());
        linkedDevice.setManufacturer(linkedDeviceDTO.manufacturer());
        linkedDevice.setProductCode(linkedDeviceDTO.productCode());
        linkedDevice.setSerialNumber(linkedDeviceDTO.serialNumber());
        linkedDevice.setComment(linkedDeviceDTO.comment());
        linkedDeviceRepo.save(linkedDevice);
        return new ResponseDTO("Linked Device added successfully");
    }

    public List<LinkedDeviceDTO> getAllLinkedDevices() {
        return linkedDeviceMapper.toDtoList(linkedDeviceRepo.findAll());
    }

    public List<LinkedDeviceDTO> getLinkedDevicesByDeviceId(Integer deviceId) {
        return linkedDeviceMapper.toDtoList(linkedDeviceRepo.findByDeviceId(deviceId));
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
}
