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
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }
        Device device = deviceOpt.get();
        Comment comment = commentService.addComment(newComment);
        device.getComments().add(comment);
        deviceRepo.save(device);
        return new ResponseDTO("Comment added successfully");
    }

    public List<CommentDTO> getDeviceComments(Integer deviceId) {
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }

        Device device = deviceOpt.get();
        return commentMapper.toDtoList(
                device.getComments()
                        .stream()
                        .sorted(Comparator.comparing(Comment::getTimestamp).reversed())
                        .toList()
        );
    }
}
