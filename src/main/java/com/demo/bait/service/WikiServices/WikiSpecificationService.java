package com.demo.bait.service.WikiServices;

import com.demo.bait.dto.WikiDTO;
import com.demo.bait.entity.Wiki;
import com.demo.bait.mapper.WikiMapper;
import com.demo.bait.repository.WikiRepo;
import com.demo.bait.specification.WikiSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class WikiSpecificationService {

    private WikiRepo wikiRepo;
    private WikiMapper wikiMapper;

    public List<WikiDTO> searchWiki(String searchTerm) {
        Specification<Wiki> combinedSpec = Specification.where(null);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Specification<Wiki> searchSpec = new WikiSpecification(searchTerm);
            combinedSpec = combinedSpec.and(searchSpec);
        }
        return wikiMapper.toDtoList(wikiRepo.findAll(combinedSpec));
    }
}
