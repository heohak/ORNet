package com.demo.bait.mapper;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.entity.ClientWorker;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientWorkerMapper {

    List<ClientWorkerDTO> toDtoList(List<ClientWorker> clientWorkerList);

    ClientWorkerDTO toDto(ClientWorker clientWorker);
}
