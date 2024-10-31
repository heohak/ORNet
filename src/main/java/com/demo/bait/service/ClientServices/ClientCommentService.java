package com.demo.bait.service.ClientServices;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Activity;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Location;
import com.demo.bait.mapper.CommentMapper;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.CommentRepo;
import com.demo.bait.service.CommentServices.CommentService;
import com.demo.bait.service.LocationServices.LocationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ClientCommentService {

    private ClientRepo clientRepo;
    private CommentRepo commentRepo;
    private CommentMapper commentMapper;
    private CommentService commentService;
    private LocationService locationService;

    @Transactional
    public ResponseDTO addCommentToClient(Integer clientId, CommentDTO commentDTO) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with ID " + clientId + " not found");
        }
        Client client = clientOpt.get();

        Comment newClientComment = commentService.addComment(commentDTO.comment());
        if (commentDTO.locationIds() != null) {
            List<Integer> locationIds = commentDTO.locationIds();
            if (locationIds.size() > 0) {
                Set<Location> locations = locationService.locationIdsToLocationsSet(locationIds);
                newClientComment.setLocations(locations);
            }
        }
        client.getComments().add(newClientComment);
        clientRepo.save(client);
        return new ResponseDTO("Comment added to client successfully");
    }

    @Transactional
    public ResponseDTO deleteClientComment(Integer clientId, Integer commentId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        Optional<Comment> commentOpt = commentRepo.findById(commentId);

        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with ID " + clientId + " not found");
        }
        if (commentOpt.isEmpty()) {
            throw new EntityNotFoundException("Comment with ID " + commentId + " not found");
        }

        Client client = clientOpt.get();
        Comment comment = commentOpt.get();
        client.getComments().remove(comment);
        clientRepo.save(client);

        comment.getLocations().clear();
        commentRepo.delete(comment);
        return new ResponseDTO("Comment deleted from client successfully");
    }

    public List<CommentDTO> getClientComments(Integer clientId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with ID " + clientId + " not found");
        }
        Client client = clientOpt.get();
        return commentMapper.toDtoList(client.getComments().stream()
                .sorted(Comparator.comparing(Comment::getTimestamp)).toList());
    }
}
