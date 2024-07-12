package com.demo.bait.controller;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.WikiDTO;
import com.demo.bait.service.WikiService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/wiki")
public class WikiController {

    public final WikiService wikiService;


    @PostMapping("/add")
    public ResponseDTO addWiki(@RequestBody WikiDTO wikiDTO) {
        return wikiService.addWiki(wikiDTO);
    }

    @GetMapping("/all")
    public List<WikiDTO> getAllWikis() {
        return wikiService.getAllWikis();
    }

    @DeleteMapping("/{wikiId}")
    public ResponseDTO deleteWiki(@PathVariable Integer wikiId) {
        return wikiService.deleteWiki(wikiId);
    }
}
