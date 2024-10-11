package com.demo.bait.controller.ActivityController;

import com.demo.bait.dto.ActivityDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ActivityService.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/activity")
public class ActivityPutController {

    public final ActivityService activityService;

    @PutMapping("/update/{activityId}")
    public ResponseDTO updateActivity(@PathVariable Integer activityId, @RequestBody ActivityDTO activityDTO) {
        return activityService.updateActivity(activityId, activityDTO);
    }
}
