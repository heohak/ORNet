package com.demo.bait.controller.ActivityController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.ActivityService.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/activity")
public class ActivityDeleteController {

    public final ActivityService activityService;

    @DeleteMapping("/delete/{activityId}")
    public ResponseDTO deleteActivity(@PathVariable Integer activityId) {
        return activityService.deleteActivity(activityId);
    }
}
