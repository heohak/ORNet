package com.demo.bait.service;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.mapper.ClientMapper;
import com.demo.bait.repository.ClientRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ClientService {

    private ClientRepo clientRepo;
    private ClientMapper clientMapper;

    public ResponseDTO addClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setFullName(clientDTO.fullName());
        client.setShortName(clientDTO.shortName());
        client.setThirdPartyIT(clientDTO.thirdPartyIT());
        clientRepo.save(client);
        return new ResponseDTO("Client added successfully");
    }

    public List<ClientDTO> getAllClients() {
        return clientMapper.toDtoList(clientRepo.findAll());
    }

    public void deleteClient(Integer id) {
        clientRepo.deleteById(id);
    }
}
