package com.demo.bait.mapper;

import com.demo.bait.dto.ClientWorkerDTO;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientWorkerMapper {

//    @Mapping(source = "client.id", target = "clientId")
//    @Mapping(source = "location.id", target = "locationId")
    List<ClientWorkerDTO> toDtoList(List<ClientWorker> clientWorkerList);

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(target = "roleIds", expression = "java(mapRolesToIds(clientWorker.getRoles()))")
    ClientWorkerDTO toDto(ClientWorker clientWorker);

    default List<Integer> mapRolesToIds(Set<ClientWorkerRoleClassificator> roles) {
        return roles.stream()
                .map(ClientWorkerRoleClassificator::getId)
                .collect(Collectors.toList());
    }
}
