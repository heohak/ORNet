package com.demo.bait.specification;

import com.demo.bait.entity.Client;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class ClientSpecification implements Specification<Client> {


    private String searchTerm;

    @Override
    public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        String likePattern = "%" + searchTerm.toLowerCase() + "%";
        Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), likePattern);
        Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("shortName")), likePattern);
        Predicate workTypePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("otherMedicalInformation")), likePattern);
        return criteriaBuilder.or(titlePredicate, descriptionPredicate, workTypePredicate);
    }
}
