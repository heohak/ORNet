package com.demo.bait.controller.classificator;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.WorkTypeClassificatorDTO;
import com.demo.bait.service.classificator.WorkTypeClassificatorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/work-type/classificator")
public class WorkTypeClassificatorController {

    public final WorkTypeClassificatorService workTypeClassificatorService;
    private RequestParamParser requestParamParser;


    @PostMapping("/add")
    public WorkTypeClassificatorDTO addWorkTypeClassificator(@RequestBody WorkTypeClassificatorDTO workTypeClassificatorDTO) {
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

    @GetMapping("/history/{workTypeId}")
    public List<WorkTypeClassificatorDTO> getWorkTypeHistory(@PathVariable String workTypeId) {
        Integer parsedWorkTypeId = requestParamParser.parseId(workTypeId, "workTypeId");
        return workTypeClassificatorService.getWorkTypeHistory(parsedWorkTypeId);
    }

    @GetMapping("/deleted")
    public List<WorkTypeClassificatorDTO> getDeletedWorkTypes() {
        return workTypeClassificatorService.getDeletedWorkTypeClassificators();
    }
}
