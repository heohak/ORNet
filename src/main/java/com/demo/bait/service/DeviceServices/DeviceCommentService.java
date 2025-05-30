package com.demo.bait.service.DeviceServices;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Device;
import com.demo.bait.mapper.CommentMapper;
import com.demo.bait.repository.DeviceRepo;
import com.demo.bait.service.CommentServices.CommentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceCommentService {

    private DeviceRepo deviceRepo;
    private CommentMapper commentMapper;
    private CommentService commentService;

    @Transactional
    public ResponseDTO addCommentToDevice(Integer deviceId, String newComment) {
        log.info("Adding comment to device with ID: {}", deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }

            Device device = deviceOpt.get();
            log.debug("Adding new comment: '{}' to device with ID: {}", newComment, deviceId);
            Comment comment = commentService.addComment(newComment);
            device.getComments().add(comment);
            deviceRepo.save(device);

            log.info("Successfully added comment to device with ID: {}", deviceId);
            return new ResponseDTO("Comment added successfully");
        } catch (Exception e) {
            log.error("Error while adding comment to device with ID: {}", deviceId, e);
            throw e;
        }
    }

    public List<CommentDTO> getDeviceComments(Integer deviceId) {
        if (deviceId == null) {
            log.warn("Device ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        log.info("Fetching comments for device with ID: {}", deviceId);
        try {
            Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
            if (deviceOpt.isEmpty()) {
                log.warn("Device with ID {} not found.", deviceId);
                throw new EntityNotFoundException("Device with id " + deviceId + " not found");
            }

            Device device = deviceOpt.get();
            List<CommentDTO> comments = commentMapper.toDtoList(
                    device.getComments()
                            .stream()
                            .sorted(Comparator.comparing(Comment::getTimestamp).reversed())
                            .toList()
            );

            log.info("Fetched {} comments for device with ID: {}", comments.size(), deviceId);
            return comments;
        } catch (Exception e) {
            log.error("Error while fetching comments for device with ID: {}", deviceId, e);
            throw e;
        }
    }
}
