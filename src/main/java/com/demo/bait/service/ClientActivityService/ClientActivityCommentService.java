package com.demo.bait.service.ClientActivityService;

import com.demo.bait.dto.ActivityDTO;
import com.demo.bait.dto.ClientActivityDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Activity;
import com.demo.bait.entity.ClientActivity;
import com.demo.bait.mapper.ActivityMapper;
import com.demo.bait.repository.ClientActivityRepo;
import com.demo.bait.service.ActivityService.ActivityService;
import com.demo.bait.service.TicketServices.TicketService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ClientActivityCommentService {

    private ClientActivityRepo clientActivityRepo;
    private ActivityMapper activityMapper;
    private ActivityService activityService;

    @Transactional
    public ResponseDTO addActivityToClientActivity(Integer clientActivityId, String newActivity) {
        log.info("Attempting to add activity to client activity with ID: {}", clientActivityId);

        Optional<ClientActivity> clientActivityOpt = clientActivityRepo.findById(clientActivityId);
        if (clientActivityOpt.isEmpty()) {
            log.error("Client activity with ID {} not found", clientActivityId);
            throw new EntityNotFoundException("Client activity with id " + clientActivityId + " not found");
        }
        ClientActivity clientActivity = clientActivityOpt.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            log.error("Unauthenticated user attempted to add activity to client activity ID: {}", clientActivityId);
            throw new SecurityException("User is not authenticated");
        }
        String username = authentication.getName();
        if (username != null && username.endsWith("@bait.local")) {
            username = username.substring(0, username.indexOf("@"));
        }
        log.debug("Authenticated user: {}", username);

        log.debug("Adding activity: '{}' to client activity with ID: {}", newActivity, clientActivity);
        Activity activity = activityService.addActivity(newActivity, null, null, null, username);

        clientActivity.getActivities().add(activity);

        clientActivityRepo.save(clientActivity);
        log.info("Activity added successfully to client activity with ID: {}", clientActivityId);
        return new ResponseDTO("Activity added successfully");
    }

    public List<ActivityDTO> getClientActivityActivities(Integer clientActivityId) {
        if (clientActivityId == null) {
            log.warn("Client Activity ID is null. Returning empty list");
            return Collections.emptyList();
        }

        log.info("Fetching activities for client activity with ID: {}", clientActivityId);

        Optional<ClientActivity> clientActivityOpt = clientActivityRepo.findById(clientActivityId);
        if (clientActivityOpt.isEmpty()) {
            log.error("Client activity with ID {} not found", clientActivityId);
            throw new EntityNotFoundException("Client activity with id " + clientActivityId + " not found");
        }
        ClientActivity clientActivity = clientActivityOpt.get();

        List<ActivityDTO> activityDTOs = activityMapper.toDtoList(
                clientActivity.getActivities()
                        .stream()
                        .sorted(Comparator.comparing(Activity::getTimestamp))
                        .toList());
        log.info("Retrieved {} activities for client activity with ID: {}", activityDTOs.size(), clientActivityId);
        return activityDTOs;
    }
}
