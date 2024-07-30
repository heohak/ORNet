package com.demo.bait.service;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.WikiDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.Wiki;
import com.demo.bait.mapper.WikiMapper;
import com.demo.bait.repository.WikiRepo;
import com.demo.bait.specification.DeviceSpecification;
import com.demo.bait.specification.WikiSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public WikiDTO getWikiById(Integer id) {
        Optional<Wiki> wikiOpt = wikiRepo.findById(id);
        if (wikiOpt.isEmpty()) {
            throw new EntityNotFoundException("Wiki with id " + id + " not found");
        }
        return wikiMapper.toDto(wikiOpt.get());
    }

    public List<WikiDTO> searchWiki(String searchTerm) {
        Specification<Wiki> combinedSpec = Specification.where(null);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Specification<Wiki> searchSpec = new WikiSpecification(searchTerm);
            combinedSpec = combinedSpec.and(searchSpec);
        }
        return wikiMapper.toDtoList(wikiRepo.findAll(combinedSpec));
    }
}
