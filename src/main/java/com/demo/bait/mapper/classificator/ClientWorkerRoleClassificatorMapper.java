package com.demo.bait.mapper.classificator;

import com.demo.bait.dto.classificator.ClientWorkerRoleClassificatorDTO;
import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientWorkerRoleClassificatorMapper {

    List<ClientWorkerRoleClassificatorDTO> toDtoList(List<ClientWorkerRoleClassificator> roleClassificatorList);
    ClientWorkerRoleClassificatorDTO toDto(ClientWorkerRoleClassificator roleClassificator);
}
