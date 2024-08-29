package com.demo.bait.service.CommentServices;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.mapper.CommentMapper;
import com.demo.bait.repository.CommentRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ResponseDTO addComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setComment(commentDTO.comment());
        comment.setTimestamp(LocalDateTime.now());
        commentRepo.save(comment);
        return new ResponseDTO("Comment added successfully");
    }

    @Transactional
    public Comment addComment(String newComment) {
        Comment comment = new Comment();
        comment.setComment(newComment);
        comment.setTimestamp(LocalDateTime.now());
        commentRepo.save(comment);
        return comment;
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
}
