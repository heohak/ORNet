package com.demo.bait.service.WikiServices;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.WikiDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.Wiki;
import com.demo.bait.mapper.WikiMapper;
import com.demo.bait.repository.WikiRepo;
import com.demo.bait.specification.DeviceSpecification;
import com.demo.bait.specification.WikiSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    @Transactional
    public ResponseDTO addWiki(WikiDTO wikiDTO) {
        Wiki wiki = new Wiki();
        wiki.setProblem(wikiDTO.problem());
        wiki.setSolution(wikiDTO.solution());
        wikiRepo.save(wiki);
        return new ResponseDTO("Wiki added successfully");
    }

    @Transactional
    public ResponseDTO deleteWiki(Integer wikiId) {
        wikiRepo.deleteById(wikiId);
        return new ResponseDTO("Wiki deleted successfully");
    }

    public List<WikiDTO> getAllWikis() {
        return wikiMapper.toDtoList(wikiRepo.findAll());
    }

    public WikiDTO getWikiById(Integer id) {
        Optional<Wiki> wikiOpt = wikiRepo.findById(id);
        if (wikiOpt.isEmpty()) {
            throw new EntityNotFoundException("Wiki with id " + id + " not found");
        }
        return wikiMapper.toDto(wikiOpt.get());
    }

    @Transactional
    public ResponseDTO updateWiki(Integer wikiId, WikiDTO wikiDTO) {
        Optional<Wiki> wikiOpt = wikiRepo.findById(wikiId);
        if (wikiOpt.isEmpty()) {
            throw new EntityNotFoundException("Wiki with id " + wikiId + " not found");
        }
        Wiki wiki = wikiOpt.get();
        wiki.setSolution(wikiDTO.solution());
        wiki.setProblem(wikiDTO.problem());
        wikiRepo.save(wiki);
        return new ResponseDTO("Wiki updated successfully");
    }
}
