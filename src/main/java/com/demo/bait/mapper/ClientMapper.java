package com.demo.bait.mapper;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.entity.ThirdPartyIT;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

//    @Mapping(target = "locationIds", expression = "java(mapLocationsToIds(client.getLocations()))")
//    @Mapping(target = "thirdPartyITIds", expression = "java(mapThirdPartyITsToIds(client.getThirdPartyITs()))")
    List<ClientDTO> toDtoList(List<Client> clientList);

    @Mapping(target = "locationIds", expression = "java(mapLocationsToIds(client.getLocations()))")
    @Mapping(target = "thirdPartyIds", expression = "java(mapThirdPartyITsToIds(client.getThirdPartyITs()))")
    @Mapping(target = "maintenanceIds", expression = "java(mapMaintenancesToIds(client.getMaintenances()))")
    ClientDTO toDto(Client client);

    default List<Integer> mapLocationsToIds(Set<Location> locations) {
        return locations.stream()
                .map(Location::getId)
                .collect(Collectors.toList());
    }

    default List<Integer> mapThirdPartyITsToIds(Set<ThirdPartyIT> thirdPartyITs) {
        return thirdPartyITs.stream()
                .map(ThirdPartyIT::getId)
                .collect(Collectors.toList());
    }

    default List<Integer> mapMaintenancesToIds(Set<Maintenance> maintenances) {
        return maintenances.stream()
                .map(Maintenance::getId)
                .collect(Collectors.toList());
    }
}
