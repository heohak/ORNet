package com.demo.bait.service.CommentServices;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.LinkedDevice;
import com.demo.bait.entity.Location;
import com.demo.bait.mapper.CommentMapper;
import com.demo.bait.repository.CommentRepo;
import com.demo.bait.repository.DeviceRepo;
import com.demo.bait.repository.LinkedDeviceRepo;
import com.demo.bait.repository.LocationRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class CommentService {

    private CommentRepo commentRepo;
    private CommentMapper commentMapper;
    private LocationRepo locationRepo;
    private DeviceRepo deviceRepo;
    private LinkedDeviceRepo linkedDeviceRepo;

    @Transactional
    public ResponseDTO addComment(CommentDTO commentDTO) {
        log.info("Adding new comment: {}", commentDTO.comment());

        if (commentDTO.comment() == null || commentDTO.comment().trim().isEmpty()) {
            log.error("Failed to add comment: Comment is null or empty");
            throw new IllegalArgumentException("Comment cannot be null or empty");
        }

        Comment comment = new Comment();
        comment.setComment(commentDTO.comment());
        comment.setTimestamp(LocalDateTime.now().withNano(0));

        String username = getUsername();
        comment.setUsername(username);

        commentRepo.save(comment);
        log.info("Comment added successfully: {}", commentDTO.comment());
        return new ResponseDTO("Comment added successfully");
    }

    @Transactional
    public Comment addComment(String newComment) {
        log.info("Adding new comment: {}", newComment);

        if (newComment == null || newComment.trim().isEmpty()) {
            log.error("Failed to add comment: Comment is null or empty");
            throw new IllegalArgumentException("Comment cannot be null or empty");
        }

        Comment comment = new Comment();
        comment.setComment(newComment);
        comment.setTimestamp(LocalDateTime.now().withNano(0));

        String username = getUsername();
        comment.setUsername(username);

        commentRepo.save(comment);
        log.info("Comment added successfully: {}", newComment);
        return comment;
    }

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getName().equals("anonymousUser")) {
            log.error("User is not authenticated");
            throw new SecurityException("User is not authenticated");
        }
        String username = authentication.getName();
        log.debug("Authenticated username: {}", username);
        return username;
    }

    public List<CommentDTO> getAllComments() {
        log.info("Fetching all comments");
        List<CommentDTO> comments = commentMapper.toDtoList(commentRepo.findAll());
        log.info("Total comments fetched: {}", comments.size());
        return comments;
    }

    public Set<Comment> commentIdsToCommentsSet(List<Integer> commentIds) {
        log.info("Fetching comments by IDs: {}", commentIds);
        Set<Comment> comments = new HashSet<>();
        for (Integer commentId : commentIds) {
            Comment comment = commentRepo.findById(commentId)
                    .orElseThrow(() -> {
                        log.error("Invalid comment ID: {}", commentId);
                        return new IllegalArgumentException("Invalid comment ID: " + commentId);
                    });
            comments.add(comment);
        }
        log.info("Fetched comments: {}", comments.size());
        return comments;
    }

    public CommentDTO getCommentById(Integer id) {
        log.info("Fetching comment by ID: {}", id);
        Optional<Comment> commentOpt = commentRepo.findById(id);
        if (commentOpt.isEmpty()) {
            log.error("Comment with ID {} not found", id);
            throw new EntityNotFoundException("Comment with ID " + id + " not found");
        }
        CommentDTO commentDTO = commentMapper.toDto(commentOpt.get());
        log.info("Fetched comment: {}", commentDTO.comment());
        return commentDTO;
    }

    @Transactional
    public ResponseDTO deleteComment(Integer commentId) {
        log.info("Deleting comment with ID: {}", commentId);

        Optional<Comment> commentOpt = commentRepo.findById(commentId);
        if (commentOpt.isEmpty()) {
            log.error("Comment with ID {} not found", commentId);
            throw new EntityNotFoundException("Comment with ID " + commentId + " not found");
        }
        Comment comment = commentOpt.get();

        log.debug("Removing comment from associated entities: {}", commentId);

        Location location = locationRepo.findByCommentsContaining(comment);
        if (location != null) {
            log.debug("Removing comment from location with ID: {}", location.getId());
            location.getComments().remove(comment);
            locationRepo.save(location);
        }

        Device device = deviceRepo.findByCommentsContaining(comment);
        if (device != null) {
            log.debug("Removing comment from device with ID: {}", device.getId());
            device.getComments().remove(comment);
            deviceRepo.save(device);
        }

        LinkedDevice linkedDevice = linkedDeviceRepo.findByCommentsContaining(comment);
        if (linkedDevice != null) {
            log.debug("Removing comment from linked device with ID: {}", linkedDevice.getId());
            linkedDevice.getComments().remove(comment);
            linkedDeviceRepo.save(linkedDevice);
        }

        commentRepo.delete(comment);
        log.info("Comment with ID {} deleted successfully", commentId);
        return new ResponseDTO("Comment deleted successfully");
    }
}
