package com.demo.bait.controller.classificator;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.WorkTypeClassificatorDTO;
import com.demo.bait.service.classificator.WorkTypeClassificatorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/work-type/classificator")
public class WorkTypeClassificatorController {

    public final WorkTypeClassificatorService workTypeClassificatorService;

    @PostMapping("/add")
    public ResponseDTO addWorkTypeClassificator(@RequestBody WorkTypeClassificatorDTO workTypeClassificatorDTO) {
        return workTypeClassificatorService.addWorkTypeClassificator(workTypeClassificatorDTO);
    }

    @GetMapping("/all")
    public List<WorkTypeClassificatorDTO> getAllWorkTypes() {
        return workTypeClassificatorService.getAllWorkTypes();
    }

    @PutMapping("/update/{workTypeId}")
    public ResponseDTO updateWorkTypeClassificator(@PathVariable Integer workTypeId,
                                                   @RequestBody WorkTypeClassificatorDTO workTypeClassificatorDTO) {
        return workTypeClassificatorService.updateWorkTypeClassificator(workTypeId, workTypeClassificatorDTO);
    }

    @DeleteMapping("/{workTypeId}")
    public ResponseDTO deleteWorkType(@PathVariable Integer workTypeId) {
        return workTypeClassificatorService.deleteWorkTypeClassificator(workTypeId);
    }
}
