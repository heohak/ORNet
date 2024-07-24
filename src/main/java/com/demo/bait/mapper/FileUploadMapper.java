package com.demo.bait.mapper;

import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.entity.FileUpload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileUploadMapper {

    List<FileUploadDTO> toDtoList(List<FileUpload> files);
    @Mapping(target = "thumbnailUrl", source = "id", qualifiedByName = "thumbnailUrlFromId")
    FileUploadDTO toDto(FileUpload file);

    @Named("thumbnailUrlFromId")
    default String thumbnailUrlFromId(Integer id) {
        return "/file/thumbnail/" + id;
    }
}
