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
import java.util.Collections;
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
        log.info("Uploading files to device with ID: {}", deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }

            Device device = deviceOpt.get();
            log.debug("Uploading {} files to device with ID: {}", files.size(), deviceId);
            Set<FileUpload> uploadedFiles = fileUploadService.uploadFiles(files);
            device.getFiles().addAll(uploadedFiles);
            deviceRepo.save(device);

            log.info("Successfully uploaded {} files to device with ID: {}", uploadedFiles.size(), deviceId);
            return new ResponseDTO("Files uploaded successfully to device");
        } catch (IOException e) {
            log.error("IO Exception occurred while uploading files to device with ID: {}", deviceId, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while uploading files to device with ID: {}", deviceId, e);
            throw e;
        }
    }

    public List<FileUploadDTO> getDeviceFiles(Integer deviceId) {
        if (deviceId == null) {
            log.warn("Device ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        log.info("Fetching files for device with ID: {}", deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }

            Device device = deviceOpt.get();
            List<FileUploadDTO> files = fileUploadMapper.toDtoList(device.getFiles().stream().toList());

            log.info("Fetched {} files for device with ID: {}", files.size(), deviceId);
            return files;
        } catch (Exception e) {
            log.error("Error while fetching files for device with ID: {}", deviceId, e);
            throw e;
        }
    }
}
