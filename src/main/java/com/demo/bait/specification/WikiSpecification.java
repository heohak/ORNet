package com.demo.bait.specification;

import com.demo.bait.entity.Wiki;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class WikiSpecification implements Specification<Wiki> {

    private String searchTerm;

    @Override
    public Predicate toPredicate(Root<Wiki> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        String likePattern = "%" + searchTerm.toLowerCase() + "%";

        Predicate wikiProblemPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("problem")), likePattern);
        Predicate wikiSolutionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("solution")), likePattern);

        return criteriaBuilder.or(wikiProblemPredicate, wikiSolutionPredicate);
    }
}
