package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.CommentMapper;
import com.demo.bait.repository.CommentRepo;
import com.demo.bait.repository.TicketRepo;
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
public class TicketCommentService {

    private TicketRepo ticketRepo;
    private CommentMapper commentMapper;
    private CommentService commentService;
    private TicketService ticketService;

    @Transactional
    public ResponseDTO addCommentToTicket(Integer ticketId, String newComment, Integer hours, Integer minutes,
                                          Boolean paid) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        ticketService.addTimeSpent(ticket, hours, minutes, paid);
        Comment comment = commentService.addComment(newComment);
        ticket.getComments().add(comment);
        ticketRepo.save(ticket);
        return new ResponseDTO("Comment added successfully");
    }

    public List<CommentDTO> getTicketComments(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        return commentMapper.toDtoList(
                ticket.getComments()
                        .stream()
                        .sorted(Comparator.comparing(Comment::getTimestamp).reversed())
                        .toList());
    }
}
