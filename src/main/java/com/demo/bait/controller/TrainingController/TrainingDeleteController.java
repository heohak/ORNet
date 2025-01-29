package com.demo.bait.controller.TrainingController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.TrainingServices.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/training")
public class TrainingDeleteController {

    public final TrainingService trainingService;

    @DeleteMapping("/{trainingId}")
    public ResponseDTO deleteTraining(@PathVariable Integer trainingId) {
        return trainingService.deleteTraining(trainingId);
    }
}
