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
        Optional<LinkedDevice> linkedDeviceOpt = linkedDeviceRepo.findById(linkedDeviceId);
        if (linkedDeviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Linked Device with id " + linkedDeviceId + " not found");
        }
        LinkedDevice linkedDevice = linkedDeviceOpt.get();
        Comment comment = commentService.addComment(newComment);
        linkedDevice.getComments().add(comment);
        linkedDeviceRepo.save(linkedDevice);
        return new ResponseDTO("Comment added successfully");
    }

    public List<CommentDTO> getLinkedDeviceComments(Integer linkedDeviceId) {
        Optional<LinkedDevice> linkedDeviceOpt = linkedDeviceRepo.findById(linkedDeviceId);
        if (linkedDeviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Linked Device with id " + linkedDeviceId + " not found");
        }
        LinkedDevice linkedDevice = linkedDeviceOpt.get();
        return commentMapper.toDtoList(linkedDevice.getComments().stream().toList());
    }
}
