package com.demo.bait.controller.CommentController;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.CommentServices.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/comment")
public class CommentPostController {

    public final CommentService commentService;

    @PostMapping("/add")
    public ResponseDTO addComment(@RequestBody CommentDTO commentDTO) {
        return commentService.addComment(commentDTO);
    }
}
