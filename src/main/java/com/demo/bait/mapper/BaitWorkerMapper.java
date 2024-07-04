package com.demo.bait.mapper;

import com.demo.bait.dto.BaitWorkerDTO;
import com.demo.bait.entity.BaitWorker;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BaitWorkerMapper {

    List<BaitWorkerDTO> toDtoList(List<BaitWorker> baitWorkerList);
    BaitWorkerDTO toDto(BaitWorker baitWorker);
}
