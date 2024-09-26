package com.demo.bait.service.LocationServices;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Location;
import com.demo.bait.mapper.CommentMapper;
import com.demo.bait.repository.LocationRepo;
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
public class LocationCommentService {

    private LocationRepo locationRepo;
    private CommentMapper commentMapper;
    private CommentService commentService;

    @Transactional
    public ResponseDTO addCommentToLocation(Integer locationId, String newComment) {
        Optional<Location> locationOpt = locationRepo.findById(locationId);
        if (locationOpt.isEmpty()) {
            throw new EntityNotFoundException("Location with id " + locationId + " not found");
        }
        Location location = locationOpt.get();
        Comment comment = commentService.addComment(newComment);
        location.getComments().add(comment);
        locationRepo.save(location);
        return new ResponseDTO("Comment added successfully");
    }

    public List<CommentDTO> getLocationComments(Integer locationId) {
        Optional<Location> locationOpt = locationRepo.findById(locationId);
        if (locationOpt.isEmpty()) {
            throw new EntityNotFoundException("Location with id " + locationId + " not found");
        }
        Location location = locationOpt.get();
        return commentMapper.toDtoList(
                location.getComments()
                        .stream()
                        .sorted(Comparator.comparing(Comment::getTimestamp).reversed())
                        .toList()
        );
    }
}
