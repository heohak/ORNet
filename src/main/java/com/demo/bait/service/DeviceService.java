package com.demo.bait.service;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.DeviceClassificator;
import com.demo.bait.mapper.DeviceMapper;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.repository.*;
import com.demo.bait.repository.classificator.DeviceClassificatorRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceService {

    private DeviceRepo deviceRepo;
    private DeviceMapper deviceMapper;
    private ClientRepo clientRepo;
    private LocationRepo locationRepo;
    private MaintenanceRepo maintenanceRepo;
    private FileUploadRepo fileUploadRepo;
    private FileUploadService fileUploadService;
    private MaintenanceMapper maintenanceMapper;
    private DeviceClassificatorRepo deviceClassificatorRepo;


    @Transactional
    public ResponseDTO addDevice(DeviceDTO deviceDTO) {
//        Device device = new Device();
//        device.setClientId(deviceDTO.clientId());
//        device.setDeviceName(deviceDTO.deviceName());
//        device.setSerialNumber(deviceDTO.serialNumber());
//        deviceRepo.save(device);
//        return new ResponseDTO("Device added successfully");

        Device device = new Device();

//        Optional<Client> clientOpt = clientRepo.findById(deviceDTO.clientId());
//        Optional<Location> locationOpt = locationRepo.findById(deviceDTO.locationId());
//        clientOpt.ifPresent(device::setClient);
//        locationOpt.ifPresent(device::setLocation);
        if (deviceDTO.clientId() != null && clientRepo.findById(deviceDTO.clientId()).isPresent()) {
            device.setClient(clientRepo.getReferenceById(deviceDTO.clientId()));
        }
        if (deviceDTO.locationId() != null && locationRepo.findById(deviceDTO.locationId()).isPresent()) {
            device.setLocation(locationRepo.getReferenceById(deviceDTO.locationId()));
        }

        device.setDeviceName(deviceDTO.deviceName());
        device.setDepartment(deviceDTO.department());
        device.setRoom(deviceDTO.room());
        device.setSerialNumber(deviceDTO.serialNumber());
        device.setLicenseNumber(deviceDTO.licenseNumber());
        device.setVersion(deviceDTO.version());
        device.setVersionUpdateDate(deviceDTO.versionUpdateDate());

        if (deviceDTO.maintenanceIds() != null) {
            Set<Maintenance> maintenances = new HashSet<>();
            for (Integer id : deviceDTO.maintenanceIds()) {
                Maintenance maintenance = maintenanceRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance ID: " + id));
                maintenances.add(maintenance);
            }
            device.setMaintenances(maintenances);
        }

        device.setFirstIPAddress(deviceDTO.firstIPAddress());
        device.setSecondIPAddress(deviceDTO.secondIPAddress());
        device.setSubnetMask(deviceDTO.subnetMask());
        device.setSoftwareKey(deviceDTO.softwareKey());
        device.setIntroducedDate(deviceDTO.introducedDate());
        device.setWrittenOffDate(deviceDTO.writtenOffDate());
        device.setComment(deviceDTO.comment());

        if (deviceDTO.fileIds() != null) {
            Set<FileUpload> files = new HashSet<>();
            for (Integer fileId : deviceDTO.fileIds()) {
                FileUpload file = fileUploadRepo.findById(fileId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid file ID: " + fileId));
                files.add(file);
            }
            device.setFiles(files);
        }

        if (deviceDTO.attributes() != null) {
            device.setAttributes(deviceDTO.attributes());
        } else {
            device.setAttributes(new HashMap<>());
        }

        deviceRepo.save(device);
        return new ResponseDTO("Device added successfully");
    }

    public List<DeviceDTO> getDevicesByClientId(Integer clientId) {
        return deviceMapper.toDtoList(deviceRepo.findByClientId(clientId));
    }

    public List<DeviceDTO> getAllDevices() {
        return deviceMapper.toDtoList(deviceRepo.findAll());
    }

    public DeviceDTO getDeviceById(Integer deviceId) {
        return deviceMapper.toDto(deviceRepo.getReferenceById(deviceId));
    }

    public ResponseDTO deleteDevice(Integer deviceId) {
        deviceRepo.deleteById(deviceId);
        return new ResponseDTO("Device deleted successfully");
    }

    @Transactional
    public ResponseDTO addMaintenanceToDevice(Integer deviceId, Integer maintenanceId) {
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
        Optional<Maintenance> maintenanceOpt = maintenanceRepo.findById(maintenanceId);

        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }
        if (maintenanceOpt.isEmpty()) {
            throw new EntityNotFoundException("Maintenance with id " + maintenanceId + " not found");
        }

        Device device = deviceOpt.get();
        Maintenance maintenance = maintenanceOpt.get();
        device.getMaintenances().add(maintenance);
        deviceRepo.save(device);
        return new ResponseDTO("Maintenance added successfully to device");
    }

    @Transactional
    public ResponseDTO uploadFilesToDevice(Integer deviceId, List<MultipartFile> files) throws IOException {
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }
        Device device = deviceOpt.get();
        Set<FileUpload> uploadedFiles = fileUploadService.uploadFiles(files);
        device.getFiles().addAll(uploadedFiles);
        deviceRepo.save(device);
        return new ResponseDTO("Files uploaded successfully to device");
    }

    @Transactional
    public ResponseDTO addLocationToDevice(Integer deviceId, Integer locationId) {
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
        Optional<Location> locationOpt = locationRepo.findById(locationId);

        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }
        if (locationOpt.isEmpty()) {
            throw new EntityNotFoundException("Location with id " + locationId + "not found");
        }

        Device device = deviceOpt.get();
        Location location = locationOpt.get();
        device.setLocation(location);
        deviceRepo.save(device);
        return new ResponseDTO("Location added successfully to device");
    }

    @Transactional
    public ResponseDTO addClientToDevice(Integer deviceId, Integer clientId) {
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
        Optional<Client> clientOpt = clientRepo.findById(clientId);

        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }

        Device device = deviceOpt.get();
        Client client = clientOpt.get();
        device.setClient(client);
        deviceRepo.save(device);
        return new ResponseDTO("Client added successfully");
    }

    @Transactional
    public ResponseDTO addClassificatorToDevice(Integer deviceId, Integer classificatorId) {
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
        Optional<DeviceClassificator> deviceClassificatorOpt = deviceClassificatorRepo.findById(classificatorId);

        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }
        if (deviceClassificatorOpt.isEmpty()) {
            throw new EntityNotFoundException("Classificator with id " + classificatorId + " not found");
        }

        Device device = deviceOpt.get();
        DeviceClassificator deviceClassificator = deviceClassificatorOpt.get();
        device.setClassificator(deviceClassificator);
        deviceRepo.save(device);
        return new ResponseDTO("Classificator added successfully");
    }

    public List<MaintenanceDTO> getDeviceMaintenances(Integer deviceId) {
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }

        Device device = deviceOpt.get();
        return maintenanceMapper.toDtoList(device.getMaintenances().stream().toList());
    }

    public DeviceDTO updateDeviceAttributes(Integer deviceId, Map<String, Object> newAttributes) {
        Device device = deviceRepo.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found"));
        device.getAttributes().putAll(newAttributes);
        Device updatedDevice = deviceRepo.save(device);
        return deviceMapper.toDto(updatedDevice);
    }

    public DeviceDTO removeDeviceAttribute(Integer deviceId, String attributeName) {
        Device device = deviceRepo.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException("Device not found"));
        device.getAttributes().remove(attributeName);
        Device updatedDevice = deviceRepo.save(device);
        return deviceMapper.toDto(updatedDevice);
    }

    public void addAttributeToAllDevices(String attributeName, Object attributeValue) {
        List<Device> devices = deviceRepo.findAll();
        for (Device device : devices) {
            device.getAttributes().put(attributeName, attributeValue);
        }
        deviceRepo.saveAll(devices);
    }
}
