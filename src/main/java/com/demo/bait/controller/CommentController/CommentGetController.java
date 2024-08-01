package com.demo.bait.controller.CommentController;

import com.demo.bait.dto.CommentDTO;
import com.demo.bait.service.CommentServices.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/comment")
public class CommentGetController {

    public final CommentService commentService;

    @GetMapping("/all")
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments();
    }
}
