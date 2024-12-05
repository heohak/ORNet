package com.demo.bait.controller.WikiController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.WikiDTO;
import com.demo.bait.service.WikiServices.WikiService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/wiki")
public class WikiPostController {

    public final WikiService wikiService;

    @PostMapping("/add")
    public ResponseDTO addWiki(@RequestBody WikiDTO wikiDTO) {
        return wikiService.addWiki(wikiDTO);
    }
}
