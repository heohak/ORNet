package com.demo.bait.controller.CommentController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.CommentDTO;
import com.demo.bait.service.CommentServices.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comment")
public class CommentGetController {

    public final CommentService commentService;
    private RequestParamParser requestParamParser;


    @GetMapping("/all")
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{commentId}")
    public CommentDTO getCommentById(@PathVariable String commentId) {
        Integer parsedCommentId = requestParamParser.parseId(commentId, "commentId");
        return commentService.getCommentById(parsedCommentId);
    }
}
