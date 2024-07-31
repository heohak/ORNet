package com.demo.bait.controller.WikiController;

import com.demo.bait.dto.WikiDTO;
import com.demo.bait.service.WikiServices.WikiService;
import com.demo.bait.service.WikiServices.WikiSpecificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/wiki")
public class WikiGetController {

    public final WikiService wikiService;
    public final WikiSpecificationService wikiSpecificationService;

    @GetMapping("/all")
    public List<WikiDTO> getAllWikis() {
        return wikiService.getAllWikis();
    }

    @GetMapping("/{wikiId}")
    public WikiDTO getWikiById(@PathVariable Integer wikiId) {
        return wikiService.getWikiById(wikiId);
    }

    @GetMapping("/search")
    public List<WikiDTO> searchWiki(@RequestParam(value = "q", required = false) String query) {
        return wikiSpecificationService.searchWiki(query);
    }
}
