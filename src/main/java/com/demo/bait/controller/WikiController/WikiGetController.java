package com.demo.bait.controller.WikiController;

import com.demo.bait.components.RequestParamParser;
import com.demo.bait.dto.WikiDTO;
import com.demo.bait.service.WikiServices.WikiService;
import com.demo.bait.service.WikiServices.WikiSpecificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/wiki")
public class WikiGetController {

    public final WikiService wikiService;
    public final WikiSpecificationService wikiSpecificationService;
    private final RequestParamParser requestParamParser;


    @GetMapping("/all")
    public List<WikiDTO> getAllWikis() {
        return wikiService.getAllWikis();
    }

    @GetMapping("/{wikiId}")
    public WikiDTO getWikiById(@PathVariable String wikiId) {
        Integer parsedWikiId = requestParamParser.parseId(wikiId, "Wiki ID");
        return wikiService.getWikiById(parsedWikiId);
    }

    @GetMapping("/search")
    public List<WikiDTO> searchWiki(@RequestParam(value = "q", required = false) String query) {
        return wikiSpecificationService.searchWiki(query);
    }
}
