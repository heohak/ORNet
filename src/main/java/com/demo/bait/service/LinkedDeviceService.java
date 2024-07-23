package com.demo.bait.service;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.LinkedDeviceDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.LinkedDevice;
import com.demo.bait.mapper.CommentMapper;
import com.demo.bait.mapper.LinkedDeviceMapper;
import com.demo.bait.repository.CommentRepo;
import com.demo.bait.repository.DeviceRepo;
import com.demo.bait.repository.LinkedDeviceRepo;
import com.sun.jdi.IntegerValue;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class LinkedDeviceService {

    private LinkedDeviceRepo linkedDeviceRepo;
    private LinkedDeviceMapper linkedDeviceMapper;
    private DeviceRepo deviceRepo;
    private CommentRepo commentRepo;
    private CommentMapper commentMapper;
    private CommentService commentService;


    public ResponseDTO addLinkedDevice(LinkedDeviceDTO linkedDeviceDTO) {
        LinkedDevice linkedDevice = new LinkedDevice();
        if (linkedDeviceDTO.deviceId() != null && deviceRepo.findById(linkedDeviceDTO.deviceId()).isPresent()) {
            linkedDevice.setDevice(deviceRepo.getReferenceById(linkedDeviceDTO.deviceId()));
        }
        linkedDevice.setName(linkedDeviceDTO.name());
        linkedDevice.setManufacturer(linkedDeviceDTO.manufacturer());
        linkedDevice.setProductCode(linkedDeviceDTO.productCode());
        linkedDevice.setSerialNumber(linkedDeviceDTO.serialNumber());
//        linkedDevice.setComment(linkedDeviceDTO.comment());
        if (linkedDeviceDTO.commentIds() != null) {
            Set<Comment> comments = new HashSet<>();
            for (Integer commentId : linkedDeviceDTO.commentIds()) {
                Comment comment = commentRepo.findById(commentId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));
                comments.add(comment);
            }
            linkedDevice.setComments(comments);
        }

        if (linkedDeviceDTO.attributes() != null) {
            linkedDevice.setAttributes(linkedDeviceDTO.attributes());
        } else {
            linkedDevice.setAttributes(new HashMap<>());
        }

        linkedDeviceRepo.save(linkedDevice);
        return new ResponseDTO(linkedDevice.getId().toString());
    }

    public List<LinkedDeviceDTO> getAllLinkedDevices() {
        return linkedDeviceMapper.toDtoList(linkedDeviceRepo.findAll());
    }

    public List<LinkedDeviceDTO> getLinkedDevicesByDeviceId(Integer deviceId) {
        return linkedDeviceMapper.toDtoList(linkedDeviceRepo.findByDeviceId(deviceId));
    }

    @Transactional
    public ResponseDTO linkDevice(Integer linkedDeviceId, Integer deviceId) {
        Optional<LinkedDevice> linkedDeviceOpt = linkedDeviceRepo.findById(linkedDeviceId);
        Optional<Device> deviceOpt = deviceRepo.findById(deviceId);

        if (linkedDeviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Linked Device with id " + linkedDeviceId + " not found");
        }
        if (deviceOpt.isEmpty()) {
            throw new EntityNotFoundException("Device with id " + deviceId + " not found");
        }

        LinkedDevice linkedDevice = linkedDeviceOpt.get();
        Device device = deviceOpt.get();
        linkedDevice.setDevice(device);
        linkedDeviceRepo.save(linkedDevice);
        return new ResponseDTO("Device linked successfully");
    }

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

    public ResponseDTO deleteLinkedDevice(Integer linkedDeviceId) {
        linkedDeviceRepo.deleteById(linkedDeviceId);
        return new ResponseDTO("Linked Device deleted");
    }

    public ResponseDTO updateLinkedDeviceAttributes(Integer linkedDeviceId, Map<String, Object> newAttributes) {
        LinkedDevice linkedDevice = linkedDeviceRepo.findById(linkedDeviceId)
                .orElseThrow(() -> new EntityNotFoundException("Linked device not found"));
        linkedDevice.getAttributes().putAll(newAttributes);
        linkedDeviceRepo.save(linkedDevice);
        return new ResponseDTO("Linked device attributes updated successfully");
    }

    public ResponseDTO removeLinkedDeviceAttribute(Integer linkedDeviceId, String attributeName) {
        LinkedDevice linkedDevice = linkedDeviceRepo.findById(linkedDeviceId)
                .orElseThrow(() -> new EntityNotFoundException("Linked device not found"));
        linkedDevice.getAttributes().remove(attributeName);
        linkedDeviceRepo.save(linkedDevice);
        return new ResponseDTO("Attribute removed successfully");
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
