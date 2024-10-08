package com.demo.bait.service.ActivityService;

import com.demo.bait.entity.Activity;
import com.demo.bait.entity.Comment;
import com.demo.bait.mapper.ActivityMapper;
import com.demo.bait.repository.ActivityRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class ActivityService {

    private ActivityRepo activityRepo;

    @Transactional
    public Activity addActivity(String newActivity, Integer hours, Integer minutes, Boolean paid) {
        Activity activity = new Activity();
        activity.setActivity(newActivity);
        activity.setTimestamp(LocalDateTime.now());
        if (hours != null && minutes != null) {
            activity.setTimeSpent(Duration.ofHours(hours).plusMinutes(minutes));
        } else if (hours != null) {
            activity.setTimeSpent(Duration.ofHours(hours));
        } else if (minutes != null) {
            activity.setTimeSpent(Duration.ofMinutes(minutes));
        } else {
            activity.setTimeSpent(Duration.ZERO);
        }
        activity.setPaid(paid);
        activityRepo.save(activity);
        return activity;
    }
}
