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
        log.info("Adding comment to location with ID: {}", locationId);
        try {
            Optional<Location> locationOpt = locationRepo.findById(locationId);
            if (locationOpt.isEmpty()) {
                log.warn("Location with ID {} not found", locationId);
                throw new EntityNotFoundException("Location with id " + locationId + " not found");
            }

            Location location = locationOpt.get();
            log.debug("Current comments for location ID {}: {}", locationId, location.getComments());

            Comment comment = commentService.addComment(newComment);
            location.getComments().add(comment);
            locationRepo.save(location);

            log.info("Comment added successfully to location with ID: {}", locationId);
            return new ResponseDTO("Comment added successfully");
        } catch (Exception e) {
            log.error("Error while adding comment to location with ID: {}", locationId, e);
            throw e;
        }
    }

    public List<CommentDTO> getLocationComments(Integer locationId) {
        log.info("Fetching comments for location with ID: {}", locationId);
        try {
            Optional<Location> locationOpt = locationRepo.findById(locationId);
            if (locationOpt.isEmpty()) {
                log.warn("Location with ID {} not found", locationId);
                throw new EntityNotFoundException("Location with id " + locationId + " not found");
            }

            Location location = locationOpt.get();
            log.debug("Fetched comments for location ID {}: {}", locationId, location.getComments());

            List<CommentDTO> comments = commentMapper.toDtoList(
                    location.getComments()
                            .stream()
                            .sorted(Comparator.comparing(Comment::getTimestamp).reversed())
                            .toList());

            log.info("Successfully fetched {} comments for location ID: {}", comments.size(), locationId);
            return comments;
        } catch (Exception e) {
            log.error("Error while fetching comments for location with ID: {}", locationId, e);
            throw e;
        }
    }
}
