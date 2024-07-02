package com.demo.bait.mapper;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

    List<ClientDTO> toDtoList(List<Client> client);
}
