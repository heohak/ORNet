package com.demo.bait.service.ActivityService;

import com.demo.bait.dto.ActivityDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Activity;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.ActivityMapper;
import com.demo.bait.repository.ActivityRepo;
import com.demo.bait.repository.TicketRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ActivityService {

    private ActivityRepo activityRepo;
    private TicketRepo ticketRepo;

    @Transactional
    public ResponseDTO updateActivity(Integer activityId, ActivityDTO activityDTO) {
        log.info("Attempting to update activity with ID: {}", activityId);

        Optional<Activity> activityOpt = activityRepo.findById(activityId);
        if (activityOpt.isEmpty()) {
            log.error("Activity with ID {} not found", activityId);
            throw new EntityNotFoundException("Activity with id " + activityId + " not found");
        }
        Activity activity = activityOpt.get();

        log.debug("Updating activity with ID: {} | New Activity: {}", activityId, activityDTO.activity());
        activity.setActivity(activityDTO.activity());
        activityRepo.save(activity);

        log.info("Activity with ID: {} updated successfully", activityId);
        return new ResponseDTO("Activity updated successfully");
    }

    @Transactional
    public ResponseDTO deleteActivity(Integer activityId) {
        log.info("Attempting to delete activity with ID: {}", activityId);

        Optional<Activity> activityOpt = activityRepo.findById(activityId);
        if (activityOpt.isEmpty()) {
            log.error("Activity with ID {} not found", activityId);
            throw new EntityNotFoundException("Activity with id " + activityId + " not found");
        }
        Activity activity = activityOpt.get();

        log.debug("Removing activity with ID: {} from associated ticket", activityId);
        Ticket ticket = ticketRepo.findByActivitiesContaining(activity);
        ticket.getActivities().remove(activity);
        ticketRepo.save(ticket);

        log.debug("Deleting activity with ID: {}", activityId);
        activityRepo.delete(activity);

        log.info("Activity with ID: {} deleted successfully", activityId);
        return new ResponseDTO("Activity deleted successfully");
    }

    @Transactional
    public Activity addActivity(String newActivity, Integer hours, Integer minutes, Boolean paid, String username) {
        log.info("Attempting to add a new activity: '{}' | Hours: {}, Minutes: {}, Paid: {}, Username: {}",
                newActivity, hours, minutes, paid, username);

        Activity activity = new Activity();
        activity.setActivity(newActivity);
        activity.setTimestamp(LocalDateTime.now().withNano(0));

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
        activity.setUsername(username);

        activityRepo.save(activity);
        log.info("New activity added successfully: '{}' | ID: {}", newActivity, activity.getId());
        return activity;
    }
}
