package com.demo.bait.controller.TrainingController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.FileUploadDTO;
import com.demo.bait.dto.TrainingDTO;
import com.demo.bait.enums.TrainingType;
import com.demo.bait.service.TrainingServices.TrainingFileUploadService;
import com.demo.bait.service.TrainingServices.TrainingService;
import com.demo.bait.service.TrainingServices.TrainingSpecificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/training")
public class TrainingGetController {

    public final TrainingService trainingService;
    public final TrainingSpecificationService trainingSpecificationService;
    private final RequestParamParser requestParamParser;
    private final TrainingFileUploadService trainingFileUploadService;


    @GetMapping("/all")
    public List<TrainingDTO> getAllTrainings() {
        return trainingService.getAllTrainings();
    }

    @GetMapping("/client/{clientId}")
    public List<TrainingDTO> getClientTrainings(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "client ID");
        return trainingService.getClientTrainings(parsedClientId);
    }

    @GetMapping("/last/{clientId}")
    public LocalDate getLastTrainingDateForClient(@PathVariable String clientId) {
        Integer parsedClientId = requestParamParser.parseId(clientId, "client ID");
        return trainingService.getLastTrainingDateForClient(parsedClientId);
    }

    @GetMapping("/search")
    public List<TrainingDTO> searchAndFilterTrainings(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "clientId", required = false) Integer clientId,
            @RequestParam(value = "locationId", required = false) Integer locationId,
            @RequestParam(value = "date", required = false) LocalDate trainingDate,
            @RequestParam(value = "type", required = false) TrainingType trainingType) {
        return trainingSpecificationService.searchAndFilterTrainings(query, clientId, locationId, trainingDate,
                trainingType);
    }

    @GetMapping("/files/{trainingId}")
    public List<FileUploadDTO> getTrainingFiles(@PathVariable String trainingId) {
        Integer parsedTrainingId = requestParamParser.parseId(trainingId, "training ID");
        return trainingFileUploadService.getTrainingFiles(parsedTrainingId);
    }

    @GetMapping("/{trainingId}")
    public TrainingDTO getTraining(@PathVariable String trainingId) {
        Integer parsedTrainingId = requestParamParser.parseId(trainingId, "training ID");
        return trainingService.getTraining(parsedTrainingId);
    }
}
