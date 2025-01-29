package com.demo.bait.specification;

import com.demo.bait.entity.Training;
import com.demo.bait.enums.TrainingType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@AllArgsConstructor
public class TrainingSpecification implements Specification<Training> {

    private String searchTerm;

    public static Specification<Training> hasClientId(Integer clientId) {
        return (root, query, criteriaBuilder) -> {
            if (clientId != null) {
                return criteriaBuilder.equal(root.get("client").get("id"), clientId);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Training> hasLocationId(Integer locationId) {
        return (root, query, criteriaBuilder) -> {
            if (locationId != null) {
                return criteriaBuilder.equal(root.get("location").get("id"), locationId);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Training> hasTrainingDate(LocalDate trainingDate) {
        return (root, query, criteriaBuilder) -> {
            if (trainingDate != null) {
                return criteriaBuilder.equal(root.get("trainingDate"), trainingDate);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Training> hasTrainingType(TrainingType trainingType) {
        return (root, query, criteriaBuilder) -> {
            if (trainingType != null) {
                return criteriaBuilder.equal(root.get("trainingType"), trainingType);
            }
            return criteriaBuilder.conjunction();
        };
    }
    @Override
    public Predicate toPredicate(Root<Training> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        String likePattern = "%" + searchTerm.toLowerCase() + "%";

        Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern);
        Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern);

        return criteriaBuilder.or(namePredicate, descriptionPredicate);
    }
}
