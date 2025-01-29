package com.demo.bait.controller.TrainingController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TrainingDTO;
import com.demo.bait.service.TrainingServices.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/training")
public class TrainingPutController {

    public final TrainingService trainingService;

    @PutMapping("/update/{trainingId}")
    public ResponseDTO updateTraining(@PathVariable Integer trainingId, @RequestBody TrainingDTO trainingDTO) {
        return trainingService.updateTraining(trainingId, trainingDTO);
    }
}
