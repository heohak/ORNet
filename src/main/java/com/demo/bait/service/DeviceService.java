package com.demo.bait.service;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Device;
import com.demo.bait.mapper.DeviceMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.DeviceRepo;
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
public class DeviceService {

    private DeviceRepo deviceRepo;
    private DeviceMapper deviceMapper;
    private ClientRepo clientRepo;

    @Transactional
    public ResponseDTO addDevice(DeviceDTO deviceDTO) {
//        Device device = new Device();
//        device.setClientId(deviceDTO.clientId());
//        device.setDeviceName(deviceDTO.deviceName());
//        device.setSerialNumber(deviceDTO.serialNumber());
//        deviceRepo.save(device);
//        return new ResponseDTO("Device added successfully");

        Optional<Client> clientOpt = clientRepo.findById(deviceDTO.clientId());

        if (clientOpt.isPresent()) {
            Device device = new Device();
            device.setClient(clientOpt.get());
            device.setDeviceName(deviceDTO.deviceName());
            device.setSerialNumber(deviceDTO.serialNumber());
            deviceRepo.save(device);
            return new ResponseDTO("Device added successfully");
        } else {
            throw new EntityNotFoundException("Client with id " + deviceDTO.clientId() + " not found");
        }
    }

    public List<DeviceDTO> getDevicesByClientId(Integer clientId) {
        return deviceMapper.toDtoList(deviceRepo.findByClientId(clientId));
    }
}
