package com.demo.bait.controller.WikiController;

import com.demo.bait.service.WikiServices.WikiService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/wiki")
public class WikiController {

    public final WikiService wikiService;
}
