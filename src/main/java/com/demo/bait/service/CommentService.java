package com.demo.bait.service;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.Comment;
import com.demo.bait.mapper.CommentMapper;
import com.demo.bait.repository.CommentRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CommentService {

    private CommentRepo commentRepo;
    private CommentMapper commentMapper;

    public ResponseDTO addComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setComment(commentDTO.comment());
        comment.setTimestamp(LocalDateTime.now());
        commentRepo.save(comment);
        return new ResponseDTO("Comment added successfully");
    }

    public List<CommentDTO> getAllComments() {
        return commentMapper.toDtoList(commentRepo.findAll());
    }
}
