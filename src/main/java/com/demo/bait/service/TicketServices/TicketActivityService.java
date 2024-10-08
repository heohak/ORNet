package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.ActivityDTO;
import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Activity;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.ActivityMapper;
import com.demo.bait.mapper.CommentMapper;
import com.demo.bait.repository.TicketRepo;
import com.demo.bait.service.ActivityService.ActivityService;
import com.demo.bait.service.CommentServices.CommentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TicketActivityService {

    private TicketRepo ticketRepo;
    private CommentMapper commentMapper;
    private CommentService commentService;
    private ActivityMapper activityMapper;
    private ActivityService activityService;
    private TicketService ticketService;

    @Transactional
    public ResponseDTO addActivityToTicket(Integer ticketId, String newActivity, Integer hours, Integer minutes,
                                          Boolean paid) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        ticketService.addTimeSpent(ticket, hours, minutes, paid);
        Activity activity = activityService.addActivity(newActivity, hours, minutes, paid);
        ticket.getActivities().add(activity);
//        Comment comment = commentService.addComment(newComment, hours, minutes);
//        ticket.getComments().add(comment);
        ticketRepo.save(ticket);
        return new ResponseDTO("Activity added successfully");
    }

    public List<ActivityDTO> getTicketActivities(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        System.out.println(ticket.getActivities().stream().toList().get(0).getActivity());
        return activityMapper.toDtoList(
                ticket.getActivities()
                        .stream()
                        .sorted(Comparator.comparing(Activity::getTimestamp).reversed())
                        .toList());
    }
}
