package com.demo.bait.controller.TrainingController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TrainingDTO;
import com.demo.bait.service.TrainingServices.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/training")
public class TrainingPostController {

    public final TrainingService trainingService;

    @PostMapping("/add")
    public ResponseDTO addTraining(@RequestBody TrainingDTO trainingDTO) {
        return trainingService.addTraining(trainingDTO);
    }
}
