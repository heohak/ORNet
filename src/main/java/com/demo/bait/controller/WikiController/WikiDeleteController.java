package com.demo.bait.controller.WikiController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.WikiServices.WikiService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/wiki")
public class WikiDeleteController {

    public final WikiService wikiService;

    @DeleteMapping("/{wikiId}")
    public ResponseDTO deleteWiki(@PathVariable Integer wikiId) {
        return wikiService.deleteWiki(wikiId);
    }
}
