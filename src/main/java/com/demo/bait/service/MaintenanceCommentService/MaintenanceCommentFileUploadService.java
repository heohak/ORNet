package com.demo.bait.service.MaintenanceCommentService;

import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.MaintenanceComment;
import com.demo.bait.mapper.FileUploadMapper;
import com.demo.bait.repository.MaintenanceCommentRepo;
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
public class MaintenanceCommentFileUploadService {

    private MaintenanceCommentRepo maintenanceCommentRepo;
    private FileUploadService fileUploadService;
    private FileUploadMapper fileUploadMapper;

    @Transactional
    public ResponseDTO uploadFilesToMaintenanceComment(Integer maintenanceCommentId, List<MultipartFile> files)
        throws IOException {
        log.info("Uploading files to maintenance comment with ID: {}", maintenanceCommentId);
        try {
            Optional<MaintenanceComment> maintenanceCommentOpt = maintenanceCommentRepo.findById(maintenanceCommentId);
            if (maintenanceCommentOpt.isEmpty()) {
                log.warn("Maintenance comment record with ID {} not found", maintenanceCommentId);
                throw new EntityNotFoundException("Maintenance comment record with ID " + maintenanceCommentId + " not found");
            }
            MaintenanceComment maintenanceComment = maintenanceCommentOpt.get();
            log.debug("Found maintenance comment with ID: {}", maintenanceCommentId);

            Set<FileUpload> uploadedFiles = fileUploadService.uploadFiles(files);
            maintenanceComment.getFiles().addAll(uploadedFiles);
            maintenanceCommentRepo.save(maintenanceComment);

            log.info("Successfully uploaded {} files to maintenance comment with ID: {}", uploadedFiles.size(), maintenanceCommentId);
            return new ResponseDTO("Files uploaded successfully to maintenance comment");
        } catch (Exception e) {
            log.error("Error while uploading files to maintenance comment with ID: {}", maintenanceCommentId, e);
            throw e;
        }
    }

    public List<FileUploadDTO> getMaintenanceCommentFiles(Integer maintenanceCommentId) {
        if (maintenanceCommentId == null) {
            log.warn("Maintenance comment ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        log.info("Fetching files for maintenance comment with ID: {}", maintenanceCommentId);
        try {
            Optional<MaintenanceComment> maintenanceCommentOpt = maintenanceCommentRepo.findById(maintenanceCommentId);
            if (maintenanceCommentOpt.isEmpty()) {
                log.warn("Maintenance comment record with ID {} not found", maintenanceCommentId);
                throw new EntityNotFoundException("Maintenance comment record with ID " + maintenanceCommentId + " not found");
            }
            MaintenanceComment maintenanceComment = maintenanceCommentOpt.get();

            List<FileUploadDTO> files = fileUploadMapper.toDtoList(maintenanceComment.getFiles().stream().toList());
            log.info("Fetched {} files for maintenance comment with ID: {}", files.size(), maintenanceCommentId);
            return files;
        } catch (Exception e) {
            log.error("Error while fetching files for maintenance comment with ID: {}", maintenanceCommentId, e);
            throw e;
        }
    }
}
