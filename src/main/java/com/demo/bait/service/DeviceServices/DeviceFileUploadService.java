package com.demo.bait.service.DeviceServices;

import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.mapper.FileUploadMapper;
import com.demo.bait.repository.DeviceRepo;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceFileUploadService {

    private DeviceRepo deviceRepo;
    private FileUploadService fileUploadService;
    private FileUploadMapper fileUploadMapper;

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

    public List<FileUploadDTO> getDeviceFiles(Integer deviceId) {
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);

        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }

        Device device = deviceOpt.get();
        return fileUploadMapper.toDtoList(device.getFiles().stream().toList());
    }
}
