package com.demo.bait.controller.TrainingController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.TrainingDTO;
import com.demo.bait.service.TrainingServices.TrainingFileUploadService;
import com.demo.bait.service.TrainingServices.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/training")
public class TrainingPutController {

    public final TrainingService trainingService;
    public final TrainingFileUploadService trainingFileUploadService;

    @PutMapping("/update/{trainingId}")
    public ResponseDTO updateTraining(@PathVariable Integer trainingId, @RequestBody TrainingDTO trainingDTO) {
        return trainingService.updateTraining(trainingId, trainingDTO);
    }

    @PutMapping("/upload/{trainingId}")
    public ResponseDTO uploadFilesToTraining(@PathVariable Integer trainingId,
                                             @RequestParam("files") List<MultipartFile> files) throws IOException {
        return trainingFileUploadService.uploadFilesToTraining(trainingId, files);
    }
}
