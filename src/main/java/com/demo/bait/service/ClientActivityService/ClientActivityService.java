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
    private ClientActivityMapper clientActivityMapper;
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
        Optional<Client> clientOpt = clientRepo.findById(clientActivityDTO.clientId());
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientActivityDTO.clientId() + " not found");
        }

        ClientActivity clientActivity = new ClientActivity();
        clientActivity.setClient(clientOpt.get());
        clientActivity.setTitle(clientActivityDTO.title());
        clientActivity.setClientNumeration(clientActivityDTO.clientNumeration());
        clientActivity.setDescription(clientActivityDTO.description());
        clientActivity.setStartDateTime(LocalDateTime.now().withNano(0));
        if (clientActivityDTO.locationId() != null
                && locationRepo.findById(clientActivityDTO.locationId()).isPresent()) {
            clientActivity.setLocation(locationRepo.getReferenceById(clientActivityDTO.locationId()));
        }

        if (clientActivityDTO.contactIds() != null) {
            Set<ClientWorker> contacts = clientWorkerService
                    .contactIdsToClientWorkersSet(clientActivityDTO.contactIds());
            clientActivity.setContacts(contacts);
        }

        if (clientActivityDTO.workTypeIds() != null) {
            Set<WorkTypeClassificator> workTypes = workTypeClassificatorService
                    .workTypeIdsToWorkTypesSet(clientActivityDTO.workTypeIds());
            clientActivity.setWorkTypes(workTypes);
        }

        if (clientActivityDTO.crisis() == null) {
            clientActivity.setCrisis(false);
        } else {
            clientActivity.setCrisis(clientActivityDTO.crisis());
        }

        if (clientActivityDTO.statusId() != null
                && ticketStatusRepo.findById(clientActivityDTO.statusId()).isPresent()) {
            clientActivity.setStatus(ticketStatusRepo.getReferenceById(clientActivityDTO.statusId()));
        }

        if (clientActivityDTO.baitWorkerId() != null
                && baitWorkerRepo.findById(clientActivityDTO.baitWorkerId()).isPresent()) {
            clientActivity.setBaitWorker(baitWorkerRepo.getReferenceById(clientActivityDTO.baitWorkerId()));
        }


        clientActivity.setEndDateTime(clientActivityDTO.endDateTime());

        if (clientActivityDTO.fileIds() != null) {
            Set<FileUpload> files = fileUploadService.fileIdsToFilesSet(clientActivityDTO.fileIds());
            clientActivity.setFiles(files);
        }
        if (clientActivityDTO.deviceIds() != null) {
            Set<Device> devices = deviceService.deviceIdsToDevicesSet(clientActivityDTO.deviceIds());
            clientActivity.setDevices(devices);
        }

        clientActivity.setPaid(Boolean.FALSE);
        clientActivity.setSettled(Boolean.FALSE);

        clientActivityRepo.save(clientActivity);
        setClientActivityUpdateTime(clientActivity);
        return new ResponseDTO(clientActivity.getId().toString());
    }

    @Transactional
    public void setClientActivityUpdateTime(ClientActivity clientActivity) {
        clientActivity.setUpdateDateTime(LocalDateTime.now().withNano(0));
        clientActivityRepo.save(clientActivity);
    }

    @Transactional
    public ResponseDTO updateClientActivity(Integer clientActivityId, ClientActivityDTO clientActivityDTO) {
        Optional<ClientActivity> clientActivityOpt = clientActivityRepo.findById(clientActivityId);
        if (clientActivityOpt.isEmpty()) {
            throw new EntityNotFoundException("Client Activity with id " + clientActivityId + " not found");
        }
        ClientActivity clientActivity = clientActivityOpt.get();

        clientActivity.setEndDateTime(clientActivityDTO.endDateTime());

        if (clientActivityDTO.title() != null) {
            clientActivity.setTitle(clientActivityDTO.title());
        }
        if (clientActivityDTO.description() != null) {
            clientActivity.setDescription(clientActivityDTO.description());
        }

        if (clientActivityDTO.clientNumeration() != null) {
            clientActivity.setClientNumeration(clientActivityDTO.clientNumeration());
        }

        if (clientActivityDTO.crisis() != null) {
            clientActivity.setCrisis(clientActivityDTO.crisis());
        }

        if (clientActivityDTO.locationId() != null) {
            locationRepo.findById(clientActivityDTO.locationId()).ifPresent(clientActivity::setLocation);
        }

        if (clientActivityDTO.statusId() != null) {
            ticketStatusRepo.findById(clientActivityDTO.statusId()).ifPresent(clientActivity::setStatus);
        }

        if (clientActivityDTO.baitWorkerId() != null) {
            baitWorkerRepo.findById(clientActivityDTO.baitWorkerId()).ifPresent(clientActivity::setBaitWorker);
        }

        if (clientActivityDTO.contactIds() != null) {
            Set<ClientWorker> contacts = clientWorkerService
                    .contactIdsToClientWorkersSet(clientActivityDTO.contactIds());
            clientActivity.setContacts(contacts);
        }

        if (clientActivityDTO.workTypeIds() != null) {
            Set<WorkTypeClassificator> workTypes = workTypeClassificatorService
                    .workTypeIdsToWorkTypesSet(clientActivityDTO.workTypeIds());
            clientActivity.setWorkTypes(workTypes);
        }

        if (clientActivityDTO.fileIds() != null) {
            Set<FileUpload> files = fileUploadService.fileIdsToFilesSet(clientActivityDTO.fileIds());
            clientActivity.setFiles(files);
        }

        if (clientActivityDTO.deviceIds() != null) {
            Set<Device> devices = deviceService.deviceIdsToDevicesSet(clientActivityDTO.deviceIds());
            clientActivity.setDevices(devices);
        }

        clientActivity.setPaid(clientActivityDTO.paid() != null ? clientActivityDTO.paid() : Boolean.FALSE);
        clientActivity.setSettled(clientActivityDTO.settled() != null ? clientActivityDTO.settled() : Boolean.FALSE);

        setClientActivityUpdateTime(clientActivity);

        clientActivityRepo.save(clientActivity);
        return new ResponseDTO("Client Activity updated successfully");
    }

    @Transactional
    public ResponseDTO deleteClientActivity(Integer clientActivityId) {
        Optional<ClientActivity> clientActivityOpt = clientActivityRepo.findById(clientActivityId);
        if (clientActivityOpt.isEmpty()) {
            throw new EntityNotFoundException("Client Activity with id " + clientActivityId + " not found");
        }

        ClientActivity clientActivity = clientActivityOpt.get();

        clientActivity.setClient(null);
        clientActivity.setLocation(null);
        clientActivity.getContacts().clear();
        clientActivity.getWorkTypes().clear();
        clientActivity.getFiles().clear();
        clientActivity.getDevices().clear();

        clientActivity.setBaitWorker(null);
        clientActivity.setStatus(null);

        clientActivityRepo.delete(clientActivity);

        return new ResponseDTO("Client Activity deleted successfully");
    }
}
