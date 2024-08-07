package com.demo.bait.service.DeviceServices;

import com.demo.bait.dto.*;
import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.DeviceClassificator;
import com.demo.bait.mapper.DeviceMapper;
import com.demo.bait.repository.*;
import com.demo.bait.repository.classificator.DeviceClassificatorRepo;
import com.demo.bait.service.CommentServices.CommentService;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import com.demo.bait.service.MaintenanceServices.MaintenanceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceService {

    private DeviceRepo deviceRepo;
    private DeviceMapper deviceMapper;
    private ClientRepo clientRepo;
    private LocationRepo locationRepo;
    private DeviceClassificatorRepo deviceClassificatorRepo;
    private FileUploadService fileUploadService;
    private CommentService commentService;
    private DeviceMaintenanceService deviceMaintenanceService;

    @Transactional
    public ResponseDTO addDevice(DeviceDTO deviceDTO) {
        Device device = new Device();

        updateClient(device, deviceDTO);
        updateLocation(device, deviceDTO);

        device.setDeviceName(deviceDTO.deviceName());
        device.setDepartment(deviceDTO.department());
        device.setRoom(deviceDTO.room());
        device.setSerialNumber(deviceDTO.serialNumber());
        device.setLicenseNumber(deviceDTO.licenseNumber());
        device.setVersion(deviceDTO.version());
        device.setVersionUpdateDate(deviceDTO.versionUpdateDate());

        deviceMaintenanceService.updateMaintenances(device, deviceDTO);

        device.setFirstIPAddress(deviceDTO.firstIPAddress());
        device.setSecondIPAddress(deviceDTO.secondIPAddress());
        device.setSubnetMask(deviceDTO.subnetMask());
        device.setSoftwareKey(deviceDTO.softwareKey());
        device.setIntroducedDate(deviceDTO.introducedDate());
        device.setWrittenOffDate(deviceDTO.writtenOffDate());

        if (deviceDTO.commentIds() != null) {
            Set<Comment> comments = commentService.commentIdsToCommentsSet(deviceDTO.commentIds());
            device.setComments(comments);
        }

        if (deviceDTO.fileIds() != null) {
            Set<FileUpload> files = fileUploadService.fileIdsToFilesSet(deviceDTO.fileIds());
            device.setFiles(files);
        }

        if (deviceDTO.attributes() != null) {
            device.setAttributes(deviceDTO.attributes());
        } else {
            device.setAttributes(new HashMap<>());
        }

        updateDeviceClassificator(device, deviceDTO);

        deviceRepo.save(device);
        return new ResponseDTO(device.getId().toString());
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

    @Transactional
    public ResponseDTO addWrittenOffDate(Integer deviceId, DeviceDTO deviceDTO) {
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);

        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }

        Device device = deviceOpt.get();
        device.setWrittenOffDate(deviceDTO.writtenOffDate());
        deviceRepo.save(device);
        return new ResponseDTO("Written off date added successfully");
    }

    @Transactional
    public ResponseDTO deleteDevice(Integer deviceId) {
        deviceRepo.deleteById(deviceId);
        return new ResponseDTO("Device deleted successfully");
    }

    @Transactional
    public ResponseDTO updateDevice(Integer deviceId, DeviceDTO deviceDTO) {
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }
        Device device = deviceOpt.get();

        updateClient(device, deviceDTO);
        updateLocation(device, deviceDTO);
        updateDeviceName(device, deviceDTO);
        updateDepartment(device, deviceDTO);
        updateRoom(device, deviceDTO);
        updateSerialNumber(device, deviceDTO);
        updateLicenceNumber(device, deviceDTO);
        updateVersion(device, deviceDTO);
        updateVersionUpdateDate(device, deviceDTO);
        deviceMaintenanceService.updateMaintenances(device, deviceDTO);
        updateFirstIPAddress(device, deviceDTO);
        updateSecondIPAddress(device, deviceDTO);
        updateSubnetMask(device, deviceDTO);
        updateSoftwareKey(device, deviceDTO);
        updateIntroducedDate(device, deviceDTO);
        updateWrittenOffDate(device, deviceDTO);

        updateDeviceClassificator(device, deviceDTO);

        deviceRepo.save(device);
        return new ResponseDTO("Device updated successfully");
    }

    public void updateClient(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.clientId() != null) {
            Optional<Client> clientOpt = clientRepo.findById(deviceDTO.clientId());
            clientOpt.ifPresent(device::setClient);
        }
    }

    public void updateLocation(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.locationId() != null) {
            Optional<Location> locationOpt = locationRepo.findById(deviceDTO.locationId());
            locationOpt.ifPresent(device::setLocation);
        }
    }

    public void updateDeviceName(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.deviceName() != null) {
            device.setDeviceName(deviceDTO.deviceName());
        }
    }

    public void updateDepartment(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.department() != null) {
            device.setDepartment(deviceDTO.department());
        }
    }

    public void updateRoom(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.room() != null) {
            device.setRoom(deviceDTO.room());
        }
    }

    public void updateSerialNumber(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.serialNumber() != null) {
            device.setSerialNumber(deviceDTO.serialNumber());
        }
    }

    public void updateLicenceNumber(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.licenseNumber() != null) {
            device.setLicenseNumber(deviceDTO.licenseNumber());
        }
    }

    public void updateVersion(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.version() != null) {
            device.setVersion(deviceDTO.version());
        }
    }

    public void updateVersionUpdateDate(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.versionUpdateDate() != null) {
            device.setVersionUpdateDate(deviceDTO.versionUpdateDate());
        }
    }

    public void updateFirstIPAddress(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.firstIPAddress() != null) {
            device.setFirstIPAddress(deviceDTO.firstIPAddress());
        }
    }

    public void updateSecondIPAddress(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.secondIPAddress() != null) {
            device.setSecondIPAddress(deviceDTO.secondIPAddress());
        }
    }

    public void updateSubnetMask(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.subnetMask() != null) {
            device.setSubnetMask(deviceDTO.subnetMask());
        }
    }

    public void updateSoftwareKey(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.softwareKey() != null) {
            device.setSoftwareKey(deviceDTO.softwareKey());
        }
    }

    public void updateIntroducedDate(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.introducedDate() != null) {
            device.setIntroducedDate(deviceDTO.introducedDate());
        }
    }

    public void updateWrittenOffDate(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.writtenOffDate() != null) {
            device.setWrittenOffDate(deviceDTO.writtenOffDate());
        }
    }

    public void updateDeviceClassificator(Device device, DeviceDTO deviceDTO) {
        if (deviceDTO.classificatorId() != null) {
            Optional<DeviceClassificator> deviceClassificatorOpt = deviceClassificatorRepo
                    .findById(deviceDTO.classificatorId());
            deviceClassificatorOpt.ifPresent(device::setClassificator);
        }
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
}
