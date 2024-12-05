package com.demo.bait.controller.CommentController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.CommentServices.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comment")
public class CommentDeleteController {

    public final CommentService commentService;

    @DeleteMapping("/{commentId}")
    public ResponseDTO deleteComment(@PathVariable Integer commentId) {
        return commentService.deleteComment(commentId);
    }
}
