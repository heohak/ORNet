package com.demo.bait.service;

import com.demo.bait.components.*;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.SoftwareDTO;
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

    public ResponseDTO addSoftware(SoftwareDTO softwareDTO) {
        Software software = new Software();

        software.setName(softwareDTO.name());
        software.setDbVersion(softwareDTO.dbVersion());

        if (softwareDTO.clientId() != null && clientRepo.findById(softwareDTO.clientId()).isPresent()) {
            software.setClient(clientRepo.getReferenceById(softwareDTO.clientId()));
        }

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

        softwareRepo.save(software);
        return new ResponseDTO("Software added successfully");
    }

    public List<SoftwareDTO> getAllSoftwareVariations() {
        return softwareMapper.toDtoList(softwareRepo.findAll());
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

    public List<SoftwareDTO> getSoftwareByClientId(Integer clientId) {
        return softwareMapper.toDtoList(softwareRepo.findByClientId(clientId));
    }

    public ResponseDTO deleteSoftware(Integer softwareId) {
        softwareRepo.deleteById(softwareId);
        return new ResponseDTO("Software deleted");
    }
}
