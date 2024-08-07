package com.demo.bait.service.SoftwareServices;

import com.demo.bait.components.*;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.SoftwareDTO;
import com.demo.bait.dto.componentDTO.*;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Software;
import com.demo.bait.mapper.SoftwareMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.SoftwareRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class SoftwareService {

    private SoftwareRepo softwareRepo;
    private SoftwareMapper softwareMapper;
    private ClientRepo clientRepo;

    @Transactional
    public ResponseDTO addSoftware(SoftwareDTO softwareDTO) {
        Software software = new Software();

        software.setName(softwareDTO.name());
        software.setDbVersion(softwareDTO.dbVersion());

        updateClient(software, softwareDTO);

        HIS his = new HIS();
        his.setVendorName(softwareDTO.his().vendorName());
        his.setVersion(softwareDTO.his().version());
        his.setUpdateDate(softwareDTO.his().updateDate());
        software.setHis(his);

        PACS pacs = new PACS();
        pacs.setVendorName(softwareDTO.pacs().vendorName());
        pacs.setVersion(softwareDTO.pacs().version());
        pacs.setUpdateDate(softwareDTO.pacs().updateDate());
        software.setPacs(pacs);

        DICOM dicom = new DICOM();
        dicom.setVendorName(softwareDTO.dicom().vendorName());
        dicom.setVersion(softwareDTO.dicom().version());
        dicom.setUpdateDate(softwareDTO.dicom().updateDate());
        software.setDicom(dicom);

        HL7 hl7 = new HL7();
        hl7.setVendorName(softwareDTO.hl7().vendorName());
        hl7.setVersion(softwareDTO.hl7().version());
        hl7.setUpdateDate(softwareDTO.hl7().updateDate());
        software.setHl7(hl7);

        LIS lis = new LIS();
        lis.setVendorName(softwareDTO.lis().vendorName());
        lis.setVersion(softwareDTO.lis().version());
        lis.setUpdateDate(softwareDTO.lis().updateDate());
        software.setLis(lis);

        ReturnImagesToLIS returnImagesToLIS = new ReturnImagesToLIS();
        returnImagesToLIS.setToReturn(softwareDTO.returnImagesToLIS().toReturn());
        returnImagesToLIS.setLink(softwareDTO.returnImagesToLIS().link());
        returnImagesToLIS.setUpdateDate(softwareDTO.returnImagesToLIS().updateDate());
        software.setReturnImagesToLIS(returnImagesToLIS);

        ORNetAPI orNetAPI = new ORNetAPI();
        orNetAPI.setVersion(softwareDTO.orNetAPI().version());
        orNetAPI.setUpdateDate(softwareDTO.orNetAPI().updateDate());
        software.setOrNetAPI(orNetAPI);

        software.setTxtIntegrationDate(softwareDTO.txtIntegrationDate());

        CustomerAPI customerAPI = new CustomerAPI();
        customerAPI.setVendorName(softwareDTO.customerAPI().vendorName());
        customerAPI.setVersion(softwareDTO.customerAPI().version());
        customerAPI.setUpdateDate(softwareDTO.customerAPI().updateDate());
        software.setCustomerAPI(customerAPI);

        ORNetAPIClient orNetAPIClient = new ORNetAPIClient();
        orNetAPIClient.setVersion(softwareDTO.orNetAPIClient().version());
        orNetAPIClient.setUpdateDate(softwareDTO.orNetAPIClient().updateDate());
        software.setOrNetAPIClient(orNetAPIClient);

        ConsultationModule consultationModule = new ConsultationModule();
        consultationModule.setVersion(softwareDTO.consultationModule().version());
        consultationModule.setUpdateDate(softwareDTO.consultationModule().updateDate());
        software.setConsultationModule(consultationModule);

        AIModule aiModule = new AIModule();
        aiModule.setVersion(softwareDTO.aiModule().version());
        aiModule.setUpdateDate(softwareDTO.aiModule().updateDate());
        software.setAiModule(aiModule);

        softwareRepo.save(software);
        return new ResponseDTO(software.getId().toString());
    }

    @Transactional
    public ResponseDTO addClientToSoftware(Integer softwareId, Integer clientId) {
        Optional<Software> softwareOpt = softwareRepo.findById(softwareId);
        Optional<Client> clientOpt = clientRepo.findById(clientId);

        if (softwareOpt.isEmpty()) {
            throw new EntityNotFoundException("Software with id " + softwareId + " not found");
        }
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }

        Software software = softwareOpt.get();
        Client client = clientOpt.get();
        software.setClient(client);
        softwareRepo.save(software);
        return new ResponseDTO("Client added to software");
    }

    @Transactional
    public ResponseDTO deleteSoftware(Integer softwareId) {
        softwareRepo.deleteById(softwareId);
        return new ResponseDTO("Software deleted");
    }

    @Transactional
    public ResponseDTO updateSoftware(Integer softwareId, SoftwareDTO softwareDTO) {
        Optional<Software> softwareOpt = softwareRepo.findById(softwareId);
        if (softwareOpt.isEmpty()) {
            throw new EntityNotFoundException("Software with id " + softwareId + " not found");
        }
        Software software = softwareOpt.get();

        updateName(software, softwareDTO);
        updateDbVersion(software, softwareDTO);
        updateClient(software, softwareDTO);

        updateHIS(software, softwareDTO);
        updatePACS(software, softwareDTO);
        updateDICOM(software, softwareDTO);
        updateHL7(software, softwareDTO);
        updateLIS(software, softwareDTO);
        updateReturnImagesToLIS(software, softwareDTO);
        updateORNetAPI(software, softwareDTO);

        updateTxtIntegrationDate(software, softwareDTO);

        updateCustomerAPI(software, softwareDTO);
        updateORNetAPIClient(software, softwareDTO);
        updateConsultationModule(software, softwareDTO);
        updateAIModule(software, softwareDTO);

        softwareRepo.save(software);
        return new ResponseDTO("Software updated successfully");
    }

    public void updateName(Software software, SoftwareDTO softwareDTO) {
        if (softwareDTO.name() != null) {
            software.setName(softwareDTO.name());
        }
    }

    public void updateDbVersion(Software software, SoftwareDTO softwareDTO) {
        if (softwareDTO.dbVersion() != null) {
            software.setDbVersion(softwareDTO.dbVersion());
        }
    }

    public void updateTxtIntegrationDate(Software software, SoftwareDTO softwareDTO) {
        if (softwareDTO.txtIntegrationDate() != null) {
            software.setTxtIntegrationDate(softwareDTO.txtIntegrationDate());
        }
    }

    public void updateClient(Software software, SoftwareDTO softwareDTO) {
        if (softwareDTO.clientId() != null) {
            Optional<Client> clientOpt = clientRepo.findById(softwareDTO.clientId());
            clientOpt.ifPresent(software::setClient);
        }
    }

    public void updateHIS(Software software, SoftwareDTO softwareDTO) {
        HISDTO hisDTO = softwareDTO.his();
        if (hisDTO == null) {
            return;
        }

        HIS his = Optional.ofNullable(software.getHis()).orElse(new HIS());

        Optional.ofNullable(hisDTO.vendorName()).ifPresent(his::setVendorName);
        Optional.ofNullable(hisDTO.version()).ifPresent(his::setVersion);
        Optional.ofNullable(hisDTO.updateDate()).ifPresent(his::setUpdateDate);

        software.setHis(his);
    }

    public void updatePACS(Software software, SoftwareDTO softwareDTO) {
        PACSDTO pacsDTO = softwareDTO.pacs();
        if (pacsDTO == null) {
            return;
        }

        PACS pacs = Optional.ofNullable(software.getPacs()).orElse(new PACS());

        Optional.ofNullable(pacsDTO.vendorName()).ifPresent(pacs::setVendorName);
        Optional.ofNullable(pacsDTO.version()).ifPresent(pacs::setVersion);
        Optional.ofNullable(pacsDTO.updateDate()).ifPresent(pacs::setUpdateDate);

        software.setPacs(pacs);
    }

    public void updateDICOM(Software software, SoftwareDTO softwareDTO) {
        DICOMDTO dicomDTO = softwareDTO.dicom();
        if (dicomDTO == null) {
            return;
        }

        DICOM dicom = Optional.ofNullable(software.getDicom()).orElse(new DICOM());

        Optional.ofNullable(dicomDTO.vendorName()).ifPresent(dicom::setVendorName);
        Optional.ofNullable(dicomDTO.version()).ifPresent(dicom::setVersion);
        Optional.ofNullable(dicomDTO.updateDate()).ifPresent(dicom::setUpdateDate);

        software.setDicom(dicom);
    }

    public void updateHL7(Software software, SoftwareDTO softwareDTO) {
        HL7DTO hl7DTO = softwareDTO.hl7();
        if (hl7DTO == null) {
            return;
        }

        HL7 hl7 = Optional.ofNullable(software.getHl7()).orElse(new HL7());

        Optional.ofNullable(hl7DTO.vendorName()).ifPresent(hl7::setVendorName);
        Optional.ofNullable(hl7DTO.version()).ifPresent(hl7::setVersion);
        Optional.ofNullable(hl7DTO.updateDate()).ifPresent(hl7::setUpdateDate);

        software.setHl7(hl7);
    }

    public void updateLIS(Software software, SoftwareDTO softwareDTO) {
        LISDTO lisDTO = softwareDTO.lis();
        if (lisDTO == null) {
            return;
        }

        LIS lis = Optional.ofNullable(software.getLis()).orElse(new LIS());

        Optional.ofNullable(lisDTO.vendorName()).ifPresent(lis::setVendorName);
        Optional.ofNullable(lisDTO.version()).ifPresent(lis::setVersion);
        Optional.ofNullable(lisDTO.updateDate()).ifPresent(lis::setUpdateDate);

        software.setLis(lis);
    }

    public void updateReturnImagesToLIS(Software software, SoftwareDTO softwareDTO) {
        ReturnImagesToLISDTO returnImagesToLISDTO = softwareDTO.returnImagesToLIS();
        if (returnImagesToLISDTO == null) {
            return;
        }

        ReturnImagesToLIS returnImagesToLIS = Optional.ofNullable(software.getReturnImagesToLIS()).orElse(new ReturnImagesToLIS());

        Optional.ofNullable(returnImagesToLISDTO.toReturn()).ifPresent(returnImagesToLIS::setToReturn);
        Optional.ofNullable(returnImagesToLISDTO.link()).ifPresent(returnImagesToLIS::setLink);
        Optional.ofNullable(returnImagesToLISDTO.updateDate()).ifPresent(returnImagesToLIS::setUpdateDate);

        software.setReturnImagesToLIS(returnImagesToLIS);
    }

    public void updateORNetAPI(Software software, SoftwareDTO softwareDTO) {
        ORNetAPIDTO orNetAPIDTO = softwareDTO.orNetAPI();
        if (orNetAPIDTO == null) {
            return;
        }

        ORNetAPI orNetAPI = Optional.ofNullable(software.getOrNetAPI()).orElse(new ORNetAPI());

        Optional.ofNullable(orNetAPIDTO.version()).ifPresent(orNetAPI::setVersion);
        Optional.ofNullable(orNetAPIDTO.updateDate()).ifPresent(orNetAPI::setUpdateDate);

        software.setOrNetAPI(orNetAPI);
    }

    public void updateCustomerAPI(Software software, SoftwareDTO softwareDTO) {
        CustomerAPIDTO customerAPIDTO = softwareDTO.customerAPI();
        if (customerAPIDTO == null) {
            return;
        }

        CustomerAPI customerAPI = Optional.ofNullable(software.getCustomerAPI()).orElse(new CustomerAPI());

        Optional.ofNullable(customerAPIDTO.vendorName()).ifPresent(customerAPI::setVendorName);
        Optional.ofNullable(customerAPIDTO.version()).ifPresent(customerAPI::setVersion);
        Optional.ofNullable(customerAPIDTO.updateDate()).ifPresent(customerAPI::setUpdateDate);

        software.setCustomerAPI(customerAPI);
    }

    public void updateORNetAPIClient(Software software, SoftwareDTO softwareDTO) {
        ORNetAPIClientDTO orNetAPIClientDTO = softwareDTO.orNetAPIClient();
        if (orNetAPIClientDTO == null) {
            return;
        }

        ORNetAPIClient orNetAPIClient = Optional.ofNullable(software.getOrNetAPIClient()).orElse(new ORNetAPIClient());

        Optional.ofNullable(orNetAPIClientDTO.version()).ifPresent(orNetAPIClient::setVersion);
        Optional.ofNullable(orNetAPIClientDTO.updateDate()).ifPresent(orNetAPIClient::setUpdateDate);

        software.setOrNetAPIClient(orNetAPIClient);
    }

    public void updateConsultationModule(Software software, SoftwareDTO softwareDTO) {
        ConsultationModuleDTO consultationModuleDTO = softwareDTO.consultationModule();
        if (consultationModuleDTO == null) {
            return;
        }

        ConsultationModule consultationModule = Optional.ofNullable(software.getConsultationModule()).orElse(new ConsultationModule());

        Optional.ofNullable(consultationModuleDTO.version()).ifPresent(consultationModule::setVersion);
        Optional.ofNullable(consultationModuleDTO.updateDate()).ifPresent(consultationModule::setUpdateDate);

        software.setConsultationModule(consultationModule);
    }

    public void updateAIModule(Software software, SoftwareDTO softwareDTO) {
        AIModuleDTO aiModuleDTO = softwareDTO.aiModule();
        if (aiModuleDTO == null) {
            return;
        }

        AIModule aiModule = Optional.ofNullable(software.getAiModule()).orElse(new AIModule());

        Optional.ofNullable(aiModuleDTO.version()).ifPresent(aiModule::setVersion);
        Optional.ofNullable(aiModuleDTO.updateDate()).ifPresent(aiModule::setUpdateDate);

        software.setAiModule(aiModule);
    }

    public List<SoftwareDTO> getSoftwareByClientId(Integer clientId) {
        return softwareMapper.toDtoList(softwareRepo.findByClientId(clientId));
    }

    public SoftwareDTO getSoftwareById(Integer softwareId) {
        return softwareMapper.toDto(softwareRepo.getReferenceById(softwareId));
    }

    public List<SoftwareDTO> getAllSoftwareVariations() {
        return softwareMapper.toDtoList(softwareRepo.findAll());
    }
}
