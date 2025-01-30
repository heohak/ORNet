package com.demo.bait.service.ThirdPartyITServices;

import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.ThirdPartyIT;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.FileUploadMapper;
import com.demo.bait.repository.ThirdPartyITRepo;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ThirdPartyITFileUploadService {

    private ThirdPartyITRepo thirdPartyITRepo;
    private FileUploadService fileUploadService;
    private FileUploadMapper fileUploadMapper;

    @Transactional
    public ResponseDTO uploadFilesToThirdPartyIT(Integer thirdPartyId, List<MultipartFile> files) throws IOException {
        log.info("Starting file upload to third party IT with ID: {}", thirdPartyId);

        Optional<ThirdPartyIT> thirdPartyITOpt = thirdPartyITRepo.findById(thirdPartyId);
        if (thirdPartyITOpt.isEmpty()) {
            log.error("Third Party IT with ID: {} not found", thirdPartyId);
            throw new EntityNotFoundException("Third Party IT with id " + thirdPartyId + " not found");
        }
        ThirdPartyIT thirdPartyIT = thirdPartyITOpt.get();

        log.debug("Uploading {} files to third party IT with ID: {}", files.size(), thirdPartyId);
        Set<FileUpload> uploadedFiles = fileUploadService.uploadFiles(files);

        thirdPartyIT.getFiles().addAll(uploadedFiles);
        thirdPartyITRepo.save(thirdPartyIT);

        log.info("Files uploaded successfully to third party IT with ID: {}. Total files uploaded: {}", thirdPartyIT,
                uploadedFiles.size());
        return new ResponseDTO("Files uploaded successfully to third party IT");
    }

    public List<FileUploadDTO> getThirdPartyITFiles(Integer thirdPartyId) {
        if (thirdPartyId == null) {
            log.warn("Third Party IT ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        log.info("Fetching files for third party IT with ID: {}", thirdPartyId);

        Optional<ThirdPartyIT> thirdPartyITOpt = thirdPartyITRepo.findById(thirdPartyId);
        if (thirdPartyITOpt.isEmpty()) {
            log.error("Third Party IT with ID: {} not found", thirdPartyId);
            throw new EntityNotFoundException("Third Party IT with id " + thirdPartyId + " not found");
        }
        ThirdPartyIT thirdPartyIT = thirdPartyITOpt.get();

        List<FileUploadDTO> files = fileUploadMapper.toDtoList(thirdPartyIT.getFiles().stream().toList());
        log.info("Found {} files for third party IT with ID: {}", files.size(), thirdPartyId);
        return files;
    }
}
