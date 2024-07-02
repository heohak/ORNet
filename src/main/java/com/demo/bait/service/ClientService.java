package com.demo.bait.service;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.mapper.ClientMapper;
import com.demo.bait.repository.ClientRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {

    private ClientRepo clientRepo;
    private ClientMapper clientMapper;

    public ResponseDTO addClient(ClientDTO clientDTO) {
        Client client = new Client();
        clientRepo.save(client);
        return new ResponseDTO("success");
    }

    public List<ClientDTO> getAllClients() {
        return clientMapper.toDtoList(clientRepo.findAll());
    }
}
