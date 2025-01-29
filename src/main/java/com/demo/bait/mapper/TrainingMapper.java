package com.demo.bait.mapper;

import com.demo.bait.dto.TrainingDTO;
import com.demo.bait.entity.BaitWorker;
import com.demo.bait.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainingMapper {

    List<TrainingDTO> toDtoList(List<Training> trainings);
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(target = "trainersIds", expression = "java(mapTrainersToIds(training.getTrainers()))")
    TrainingDTO toDto(Training training);

    default List<Integer> mapTrainersToIds(Set<BaitWorker> trainers) {
        return trainers.stream()
                .map(BaitWorker::getId)
                .collect(Collectors.toList());
    }
}
