package com.demo.bait.controller.CommentController;

import com.demo.bait.service.CommentServices.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    public final CommentService commentService;
}
