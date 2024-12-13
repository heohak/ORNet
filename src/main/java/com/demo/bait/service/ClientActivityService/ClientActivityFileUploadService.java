package com.demo.bait.service.ClientActivityService;

import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.ClientActivity;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.mapper.FileUploadMapper;
import com.demo.bait.repository.ClientActivityRepo;
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
public class ClientActivityFileUploadService {

    private ClientActivityRepo clientActivityRepo;
    private FileUploadService fileUploadService;
    private FileUploadMapper fileUploadMapper;

    @Transactional
    public ResponseDTO uploadFilesToClientActivity(Integer clientActivityId, List<MultipartFile> files)
            throws IOException {
        log.info("Uploading files to Client Activity with ID: {}", clientActivityId);
        try {
            Optional<ClientActivity> clientActivityOpt = clientActivityRepo.findById(clientActivityId);
            if (clientActivityOpt.isEmpty()) {
                log.warn("Client Activity with ID {} not found", clientActivityId);
                throw new EntityNotFoundException("Client Activity with id " + clientActivityId + " not found");
            }
            ClientActivity clientActivity = clientActivityOpt.get();

            log.debug("Uploading {} files for Client Activity with ID: {}", files.size(), clientActivityId);
            Set<FileUpload> uploadedFiles = fileUploadService.uploadFiles(files);

            log.debug("Adding uploaded files to Client Activity with ID: {}", clientActivityId);
            clientActivity.getFiles().addAll(uploadedFiles);

            clientActivityRepo.save(clientActivity);
            log.info("Successfully uploaded {} files to Client Activity with ID: {}", files.size(), clientActivityId);
            return new ResponseDTO("Files uploaded to client activity successfully.");
        } catch (IOException e) {
            log.error("Error while uploading files to Client Activity with ID: {}", clientActivityId, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while uploading files to Client Activity with ID: {}", clientActivityId, e);
            throw e;
        }
    }

    public List<FileUploadDTO> getClientActivityFiles(Integer clientActivityId) {
        log.info("Fetching files for Client Activity with ID: {}", clientActivityId);
        try {
            Optional<ClientActivity> clientActivityOpt = clientActivityRepo.findById(clientActivityId);
            if (clientActivityOpt.isEmpty()) {
                log.warn("Client Activity with ID {} not found", clientActivityId);
                throw new EntityNotFoundException("Client Activity with id " + clientActivityId + " not found");
            }
            ClientActivity clientActivity = clientActivityOpt.get();

            List<FileUploadDTO> fileDtos = fileUploadMapper.toDtoList(clientActivity.getFiles().stream().toList());
            log.debug("Fetched {} files for Client Activity with ID: {}", fileDtos.size(), clientActivityId);
            return fileDtos;
        } catch (Exception e) {
            log.error("Error while fetching files for Client Activity with ID: {}", clientActivityId, e);
            throw e;
        }
    }
}
