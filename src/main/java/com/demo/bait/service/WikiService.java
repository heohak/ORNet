package com.demo.bait.service;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.WikiDTO;
import com.demo.bait.entity.Wiki;
import com.demo.bait.mapper.WikiMapper;
import com.demo.bait.repository.WikiRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class WikiService {

    private WikiRepo wikiRepo;
    private WikiMapper wikiMapper;


    public ResponseDTO addWiki(WikiDTO wikiDTO) {
        Wiki wiki = new Wiki();
        wiki.setProblem(wikiDTO.problem());
        wiki.setSolution(wikiDTO.solution());
        wikiRepo.save(wiki);
        return new ResponseDTO("Wiki added successfully");
    }

    public List<WikiDTO> getAllWikis() {
        return wikiMapper.toDtoList(wikiRepo.findAll());
    }

    public ResponseDTO deleteWiki(Integer wikiId) {
        wikiRepo.deleteById(wikiId);
        return new ResponseDTO("Wiki deleted successfully");
    }
}
