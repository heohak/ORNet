package com.demo.bait.service;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.MaintenanceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.*;
import com.demo.bait.mapper.DeviceMapper;
import com.demo.bait.mapper.MaintenanceMapper;
import com.demo.bait.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Transactional
    public ResponseDTO addDevice(DeviceDTO deviceDTO) {
//        Device device = new Device();
//        device.setClientId(deviceDTO.clientId());
//        device.setDeviceName(deviceDTO.deviceName());
//        device.setSerialNumber(deviceDTO.serialNumber());
//        deviceRepo.save(device);
//        return new ResponseDTO("Device added successfully");

        Optional<Client> clientOpt = clientRepo.findById(deviceDTO.clientId());
        Optional<Location> locationOpt = locationRepo.findById(deviceDTO.locationId());
        Device device = new Device();

        clientOpt.ifPresent(device::setClient);
        locationOpt.ifPresent(device::setLocation);

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

    public List<MaintenanceDTO> getDeviceMaintenances(Integer deviceId) {
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }

        Device device = deviceOpt.get();
        return maintenanceMapper.toDtoList(device.getMaintenances().stream().toList());
    }
}
