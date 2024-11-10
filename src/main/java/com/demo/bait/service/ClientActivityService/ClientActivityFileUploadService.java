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
        Optional<ClientActivity> clientActivityOpt = clientActivityRepo.findById(clientActivityId);
        if (clientActivityOpt.isEmpty()) {
            throw new EntityNotFoundException("Client Activity with id " + clientActivityId + " not found");
        }
        ClientActivity clientActivity = clientActivityOpt.get();
        Set<FileUpload> uploadedFiles = fileUploadService.uploadFiles(files);
        clientActivity.getFiles().addAll(uploadedFiles);
        clientActivityRepo.save(clientActivity);
        return new ResponseDTO("Files uploaded to client activity successfully.");
    }

    public List<FileUploadDTO> getClientActivityFiles(Integer clientActivityId) {
        Optional<ClientActivity> clientActivityOpt = clientActivityRepo.findById(clientActivityId);
        if (clientActivityOpt.isEmpty()) {
            throw new EntityNotFoundException("Client Activity with id " + clientActivityId + " not found");
        }
        ClientActivity clientActivity = clientActivityOpt.get();
        return fileUploadMapper.toDtoList(clientActivity.getFiles().stream().toList());
    }
}
