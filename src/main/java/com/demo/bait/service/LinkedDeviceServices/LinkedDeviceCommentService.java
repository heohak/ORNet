package com.demo.bait.service.LinkedDeviceServices;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.LinkedDevice;
import com.demo.bait.mapper.CommentMapper;
import com.demo.bait.repository.CommentRepo;
import com.demo.bait.repository.LinkedDeviceRepo;
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
public class LinkedDeviceCommentService {

    private LinkedDeviceRepo linkedDeviceRepo;
    private CommentMapper commentMapper;
    private CommentService commentService;

    @Transactional
    public ResponseDTO addCommentToLinkedDevice(Integer linkedDeviceId, String newComment) {
        log.info("Adding comment to linked device with ID: {}", linkedDeviceId);
        try {
            Optional<LinkedDevice> linkedDeviceOpt = linkedDeviceRepo.findById(linkedDeviceId);
            if (linkedDeviceOpt.isEmpty()) {
                log.warn("Linked Device with ID {} not found", linkedDeviceId);
                throw new EntityNotFoundException("Linked Device with id " + linkedDeviceId + " not found");
            }

            LinkedDevice linkedDevice = linkedDeviceOpt.get();
            log.debug("Current comments for linked device ID {}: {}", linkedDeviceId, linkedDevice.getComments());

            Comment comment = commentService.addComment(newComment);
            linkedDevice.getComments().add(comment);
            linkedDeviceRepo.save(linkedDevice);

            log.info("Comment added successfully to linked device with ID: {}", linkedDeviceId);
            return new ResponseDTO("Comment added successfully");
        } catch (Exception e) {
            log.error("Error while adding comment to linked device with ID: {}", linkedDeviceId, e);
            throw e;
        }
    }

    public List<CommentDTO> getLinkedDeviceComments(Integer linkedDeviceId) {
        log.info("Fetching comments for linked device with ID: {}", linkedDeviceId);
        try {
            Optional<LinkedDevice> linkedDeviceOpt = linkedDeviceRepo.findById(linkedDeviceId);
            if (linkedDeviceOpt.isEmpty()) {
                log.warn("Linked Device with ID {} not found", linkedDeviceId);
                throw new EntityNotFoundException("Linked Device with id " + linkedDeviceId + " not found");
            }

            LinkedDevice linkedDevice = linkedDeviceOpt.get();
            log.debug("Fetched comments for linked device ID {}: {}", linkedDeviceId, linkedDevice.getComments());

            List<CommentDTO> comments = commentMapper.toDtoList(
                    linkedDevice.getComments()
                            .stream()
                            .sorted(Comparator.comparing(Comment::getTimestamp).reversed())
                            .toList());

            log.info("Successfully fetched {} comments for linked device ID: {}", comments.size(), linkedDeviceId);
            return comments;
        } catch (Exception e) {
            log.error("Error while fetching comments for linked device with ID: {}", linkedDeviceId, e);
            throw e;
        }
    }
}
