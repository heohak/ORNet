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
        if (commentDTO.comment() == null || commentDTO.comment().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment cannot be null or empty");
        }

        Comment comment = new Comment();
        comment.setComment(commentDTO.comment());
        comment.setTimestamp(LocalDateTime.now().withNano(0));
        commentRepo.save(comment);
        return new ResponseDTO("Comment added successfully");
    }

    @Transactional
    public Comment addComment(String newComment) {
        if (newComment == null || newComment.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment cannot be null or empty");
        }

        Comment comment = new Comment();
        comment.setComment(newComment);
        comment.setTimestamp(LocalDateTime.now().withNano(0));
        commentRepo.save(comment);
        return comment;
    }

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getName().equals("anonymousUser")) {
            throw new SecurityException("User is not authenticated");
        }
        return authentication.getName();
    }

    public List<CommentDTO> getAllComments() {
        return commentMapper.toDtoList(commentRepo.findAll());
    }

    public Set<Comment> commentIdsToCommentsSet(List<Integer> commentIds) {
        Set<Comment> comments = new HashSet<>();
        for (Integer commentId : commentIds) {
            Comment comment = commentRepo.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));
            comments.add(comment);
        }
        return comments;
    }

    public CommentDTO getCommentById(Integer id) {
        Optional<Comment> commentOpt = commentRepo.findById(id);
        if (commentOpt.isEmpty()) {
            throw new EntityNotFoundException("Comment with ID " + id + " not found");
        }
        Comment comment = commentOpt.get();
        return commentMapper.toDto(comment);
    }

    @Transactional
    public ResponseDTO deleteComment(Integer commentId) {
        Optional<Comment> commentOpt = commentRepo.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new EntityNotFoundException("Comment with ID " + commentId + " not found");
        }
        Comment comment = commentOpt.get();

        Location location = locationRepo.findByCommentsContaining(comment);
        if (location != null) {
            location.getComments().remove(comment);
            locationRepo.save(location);
        }

        Device device = deviceRepo.findByCommentsContaining(comment);
        if (device != null) {
            device.getComments().remove(comment);
            deviceRepo.save(device);
        }

        LinkedDevice linkedDevice = linkedDeviceRepo.findByCommentsContaining(comment);
        if (linkedDevice != null) {
            linkedDevice.getComments().remove(comment);
            linkedDeviceRepo.save(linkedDevice);
        }

        commentRepo.delete(comment);
        return new ResponseDTO("Comment deleted successfully");
    }
}
