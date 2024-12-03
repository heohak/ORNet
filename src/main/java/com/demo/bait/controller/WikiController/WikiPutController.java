package com.demo.bait.controller.WikiController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.WikiDTO;
import com.demo.bait.service.WikiServices.WikiService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/wiki")
public class WikiPutController {

    public final WikiService wikiService;

    @PutMapping("/update/{wikiId}")
    public ResponseDTO updateWiki(@PathVariable Integer wikiId, @RequestBody WikiDTO wikiDTO) {
        return wikiService.updateWiki(wikiId, wikiDTO);
    }
}
