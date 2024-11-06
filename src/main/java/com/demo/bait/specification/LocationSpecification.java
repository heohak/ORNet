package com.demo.bait.specification;

import com.demo.bait.entity.Location;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class LocationSpecification implements Specification<Location> {

    private String searchTerm;
    @Override
    public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        String likePattern = "%" + searchTerm.toLowerCase() + "%";

        Predicate locationNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern);
        Predicate locationCountryPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("country")), likePattern);
        Predicate locationCityPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), likePattern);


        return criteriaBuilder.or(locationNamePredicate,locationCountryPredicate, locationCityPredicate);
    }
}
