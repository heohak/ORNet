package com.demo.bait.service.ClientActivityService;

import com.demo.bait.dto.ClientActivityDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import com.demo.bait.mapper.ClientActivityMapper;
import com.demo.bait.repository.BaitWorkerRepo;
import com.demo.bait.repository.ClientActivityRepo;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.LocationRepo;
import com.demo.bait.repository.classificator.TicketStatusClassificatorRepo;
import com.demo.bait.service.ClientWorkerServices.ClientWorkerService;
import com.demo.bait.service.DeviceServices.DeviceService;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import com.demo.bait.service.classificator.WorkTypeClassificatorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ClientActivityService {

    private ClientActivityRepo clientActivityRepo;
    private ClientRepo clientRepo;
    private LocationRepo locationRepo;
    private ClientWorkerService clientWorkerService;
    private WorkTypeClassificatorService workTypeClassificatorService;
    private TicketStatusClassificatorRepo ticketStatusRepo;
    private BaitWorkerRepo baitWorkerRepo;
    private FileUploadService fileUploadService;
    private DeviceService deviceService;

    @Transactional
    public ResponseDTO addClientActivity(ClientActivityDTO clientActivityDTO) {
        log.info("Adding a new Client Activity: {}", clientActivityDTO);
        try {
            ClientActivity clientActivity = new ClientActivity();
            updateClientActivityFields(clientActivity, clientActivityDTO);
            clientActivity.setStartDateTime(LocalDateTime.now().withNano(0));
            clientActivity.setPaid(false);
            clientActivity.setSettled(false);

            clientActivityRepo.save(clientActivity);
            setClientActivityUpdateTime(clientActivity);
            log.info("Successfully added Client Activity with ID: {}", clientActivity.getId());
            return new ResponseDTO(clientActivity.getId().toString());
        } catch (Exception e) {
            log.error("Error while adding Client Activity: {}", clientActivityDTO, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO updateClientActivity(Integer clientActivityId, ClientActivityDTO clientActivityDTO) {
        log.info("Updating Client Activity with ID: {}", clientActivityId);
        try {
            ClientActivity clientActivity = clientActivityRepo.findById(clientActivityId)
                    .orElseThrow(() -> {
                        log.warn("Client Activity with ID {} not found", clientActivityId);
                        return new EntityNotFoundException("Client Activity with id " + clientActivityId + " not found");
                    });

            updateClientActivityFields(clientActivity, clientActivityDTO);
            setClientActivityUpdateTime(clientActivity);
            clientActivityRepo.save(clientActivity);
            log.info("Successfully updated Client Activity with ID: {}", clientActivityId);
            return new ResponseDTO("Client Activity updated successfully");
        } catch (Exception e) {
            log.error("Error while updating Client Activity with ID: {}", clientActivityId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO deleteClientActivity(Integer clientActivityId) {
        log.info("Deleting Client Activity with ID: {}", clientActivityId);
        try {
            ClientActivity clientActivity = clientActivityRepo.findById(clientActivityId)
                    .orElseThrow(() -> {
                        log.warn("Client Activity with ID {} not found", clientActivityId);
                        return new EntityNotFoundException("Client Activity with id " + clientActivityId + " not found");
                    });

            log.debug("Clearing associated entities for Client Activity with ID: {}", clientActivityId);
            clientActivity.setClient(null);
            clientActivity.setLocation(null);
            clientActivity.getContacts().clear();
            clientActivity.getWorkTypes().clear();
            clientActivity.getFiles().clear();
            clientActivity.getDevices().clear();
            clientActivity.setBaitWorker(null);
            clientActivity.setStatus(null);

            clientActivityRepo.delete(clientActivity);
            log.info("Successfully deleted Client Activity with ID: {}", clientActivityId);
            return new ResponseDTO("Client Activity deleted successfully");
        } catch (Exception e) {
            log.error("Error while deleting Client Activity with ID: {}", clientActivityId, e);
            throw e;
        }
    }

    @Transactional
    public void setClientActivityUpdateTime(ClientActivity clientActivity) {
        log.debug("Setting update time for Client Activity with ID: {}", clientActivity.getId());
        clientActivity.setUpdateDateTime(LocalDateTime.now().withNano(0));
        clientActivityRepo.save(clientActivity);
    }

    private void updateClientActivityFields(ClientActivity clientActivity, ClientActivityDTO clientActivityDTO) {
        log.debug("Updating fields for Client Activity: {}", clientActivityDTO);

        if (clientActivityDTO.clientId() != null) {
            clientRepo.findById(clientActivityDTO.clientId()).ifPresent(clientActivity::setClient);
        }

        if (clientActivityDTO.title() != null) {
            clientActivity.setTitle(clientActivityDTO.title());
        }

        if (clientActivityDTO.clientNumeration() != null) {
            clientActivity.setClientNumeration(clientActivityDTO.clientNumeration());
        }

        if (clientActivityDTO.description() != null) {
            clientActivity.setDescription(clientActivityDTO.description());
        }

        if (clientActivityDTO.locationId() != null) {
            locationRepo.findById(clientActivityDTO.locationId()).ifPresent(clientActivity::setLocation);
        }

        if (clientActivityDTO.contactIds() != null) {
            Set<ClientWorker> contacts = clientWorkerService.contactIdsToClientWorkersSet(clientActivityDTO.contactIds());
            clientActivity.setContacts(contacts);
        }

        if (clientActivityDTO.workTypeIds() != null) {
            Set<WorkTypeClassificator> workTypes = workTypeClassificatorService.workTypeIdsToWorkTypesSet(clientActivityDTO.workTypeIds());
            clientActivity.setWorkTypes(workTypes);
        }

        clientActivity.setCrisis(clientActivityDTO.crisis() != null ? clientActivityDTO.crisis() : false);

        if (clientActivityDTO.statusId() != null) {
            ticketStatusRepo.findById(clientActivityDTO.statusId()).ifPresent(clientActivity::setStatus);
        }

        if (clientActivityDTO.baitWorkerId() != null) {
            baitWorkerRepo.findById(clientActivityDTO.baitWorkerId()).ifPresent(clientActivity::setBaitWorker);
        }

        if (clientActivityDTO.endDateTime() != null) {
            clientActivity.setEndDateTime(clientActivityDTO.endDateTime());
        }

        if (clientActivityDTO.fileIds() != null) {
            Set<FileUpload> files = fileUploadService.fileIdsToFilesSet(clientActivityDTO.fileIds());
            clientActivity.setFiles(files);
        }

        if (clientActivityDTO.deviceIds() != null) {
            Set<Device> devices = deviceService.deviceIdsToDevicesSet(clientActivityDTO.deviceIds());
            clientActivity.setDevices(devices);
        }

        clientActivity.setPaid(clientActivityDTO.paid() != null ? clientActivityDTO.paid() : false);
        clientActivity.setSettled(clientActivityDTO.settled() != null ? clientActivityDTO.settled() : false);
    }
}
