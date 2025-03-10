package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.ActivityDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Activity;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.ActivityMapper;
import com.demo.bait.repository.TicketRepo;
import com.demo.bait.service.ActivityService.ActivityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TicketActivityService {

    private TicketRepo ticketRepo;
    private ActivityMapper activityMapper;
    private ActivityService activityService;
    private TicketService ticketService;

    @Transactional
    public ResponseDTO addActivityToTicket(Integer ticketId, String newActivity, Integer hours, Integer minutes,
                                           Boolean paid) {
        log.info("Attempting to add activity to ticket with ID: {}", ticketId);

        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            log.error("Ticket with ID {} not found", ticketId);
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();

        log.debug("Adding time spent to ticket with ID: {} | Hours: {}, Minutes: {}, Paid: {}",
                ticketId, hours, minutes, paid);
        ticketService.addTimeSpent(ticket, hours, minutes, paid);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            log.error("Unauthenticated user attempted to add activity to ticket ID: {}", ticketId);
            throw new SecurityException("User is not authenticated");
        }
        String username = authentication.getName();
        if (username != null && username.endsWith("@bait.local")) {
            username = username.substring(0, username.indexOf("@"));
        }
        log.debug("Authenticated user: {}", username);

        log.debug("Adding activity: '{}' to ticket with ID: {}", newActivity, ticketId);
        Activity activity = activityService.addActivity(newActivity, hours, minutes, paid, username);

//        Activity activity = activityService.addActivity(newActivity, hours, minutes, paid, null);
        ticket.getActivities().add(activity);

        ticketRepo.save(ticket);
        log.info("Activity added successfully to ticket with ID: {}", ticketId);
        return new ResponseDTO("Activity added successfully");
    }

    public List<ActivityDTO> getTicketActivities(Integer ticketId) {
        if (ticketId == null) {
            log.warn("Ticket ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        log.info("Fetching activities for ticket with ID: {}", ticketId);

        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            log.error("Ticket with ID {} not found", ticketId);
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();

        List<ActivityDTO> activities = activityMapper.toDtoList(
                ticket.getActivities()
                        .stream()
                        .sorted(Comparator.comparing(Activity::getTimestamp))
                        .toList());
        log.info("Retrieved {} activities for ticket with ID: {}", activities.size(), ticketId);
        return activities;
    }
}
