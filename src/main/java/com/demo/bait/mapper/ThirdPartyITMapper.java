package com.demo.bait.mapper;

import com.demo.bait.dto.ThirdPartyITDTO;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.ThirdPartyIT;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ThirdPartyITMapper {

    List<ThirdPartyITDTO> toDtoList(List<ThirdPartyIT> thirdPartyITList);
    @Mapping(source = "contact.id", target = "contactId")
    @Mapping(target = "fileIds", expression = "java(mapFilesToIds(thirdPartyIT.getFiles()))")
    ThirdPartyITDTO toDto(ThirdPartyIT thirdPartyIT);

    default List<Integer> mapFilesToIds(Set<FileUpload> files) {
        return files.stream()
                .map(FileUpload::getId)
                .collect(Collectors.toList());
    }
}
