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
        log.info("Adding a new wiki entry with problem: {}", wikiDTO.problem());
        Wiki wiki = new Wiki();
        wiki.setProblem(wikiDTO.problem());
        wiki.setSolution(wikiDTO.solution());
        wikiRepo.save(wiki);
        log.info("Wiki entry added successfully with ID: {}", wiki.getId());
        return new ResponseDTO("Wiki added successfully");
    }

    @Transactional
    public ResponseDTO deleteWiki(Integer wikiId) {
        log.info("Deleting wiki entry with ID: {}", wikiId);
        if (!wikiRepo.existsById(wikiId)) {
            log.error("Wiki entry with ID {} not found for deletion", wikiId);
            throw new EntityNotFoundException("Wiki with id " + wikiId + " not found");
        }
        wikiRepo.deleteById(wikiId);
        log.info("Wiki entry with ID {} deleted successfully", wikiId);
        return new ResponseDTO("Wiki deleted successfully");
    }

    public List<WikiDTO> getAllWikis() {
        log.info("Fetching all wiki entries");
        List<WikiDTO> wikis = wikiMapper.toDtoList(wikiRepo.findAll());
        log.info("Found {} wiki entries", wikis.size());
        return wikis;
    }

    public WikiDTO getWikiById(Integer id) {
        if (id == null) {
            log.warn("Wiki ID is null. Returning null.");
            return null;
        }

        log.info("Fetching wiki entry with ID: {}", id);
        Optional<Wiki> wikiOpt = wikiRepo.findById(id);
        if (wikiOpt.isEmpty()) {
            log.error("Wiki entry with ID {} not found", id);
            throw new EntityNotFoundException("Wiki with id " + id + " not found");
        }
        log.info("Wiki entry with ID {} fetched successfully", id);
        return wikiMapper.toDto(wikiOpt.get());
    }

    @Transactional
    public ResponseDTO updateWiki(Integer wikiId, WikiDTO wikiDTO) {
        log.info("Updating wiki entry with ID: {}", wikiId);
        Optional<Wiki> wikiOpt = wikiRepo.findById(wikiId);
        if (wikiOpt.isEmpty()) {
            log.error("Wiki entry with ID {} not found for update", wikiId);
            throw new EntityNotFoundException("Wiki with id " + wikiId + " not found");
        }
        Wiki wiki = wikiOpt.get();
        log.debug("Updating wiki problem to: {}", wikiDTO.problem());
        log.debug("Updating wiki solution to: {}", wikiDTO.solution());
        wiki.setSolution(wikiDTO.solution());
        wiki.setProblem(wikiDTO.problem());
        wikiRepo.save(wiki);
        log.info("Wiki entry with ID {} updated successfully", wikiId);
        return new ResponseDTO("Wiki updated successfully");
    }
}
