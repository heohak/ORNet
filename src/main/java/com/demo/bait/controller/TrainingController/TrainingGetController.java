package com.demo.bait.controller.TrainingController;

import com.demo.bait.dto.TrainingDTO;
import com.demo.bait.enums.TrainingType;
import com.demo.bait.service.TrainingServices.TrainingService;
import com.demo.bait.service.TrainingServices.TrainingSpecificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/training")
public class TrainingGetController {

    public final TrainingService trainingService;
    public final TrainingSpecificationService trainingSpecificationService;

    @GetMapping("/all")
    public List<TrainingDTO> getAllTrainings() {
        return trainingService.getAllTrainings();
    }

    @GetMapping("/client/{clientId}")
    public List<TrainingDTO> getClientTrainings(@PathVariable Integer clientId) {
        return trainingService.getClientTrainings(clientId);
    }

    @GetMapping("/last/{clientId}")
    public LocalDate getLastTrainingDateForClient(@PathVariable Integer clientId) {
        return trainingService.getLastTrainingDateForClient(clientId);
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
}
