package com.demo.bait.mapper;

import com.demo.bait.dto.ClientDTO;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Location;
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

    @Mapping(target = "locationIds", expression = "java(mapLocationsToIds(client.getLocations()))")
    List<ClientDTO> toDtoList(List<Client> clientList);

    @Mapping(target = "locationIds", expression = "java(mapLocationsToIds(client.getLocations()))")
    ClientDTO toDto(Client client);

    default List<Integer> mapLocationsToIds(Set<Location> locations) {
        return locations.stream()
                .map(Location::getId)
                .collect(Collectors.toList());
    }
}
