package com.demo.bait.mapper;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.entity.ClientWorker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientWorkerMapper {

    @Mapping(source = "client.id", target = "clientId")
    List<ClientWorkerDTO> toDtoList(List<ClientWorker> clientWorkerList);

    @Mapping(source = "client.id", target = "clientId")
    ClientWorkerDTO toDto(ClientWorker clientWorker);
}
