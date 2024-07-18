package com.demo.bait.specification;

import com.demo.bait.entity.Ticket;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;


@AllArgsConstructor
public class TicketSpecification implements Specification<Ticket> {

    private String searchTerm;

    public static Specification<Ticket> hasStatusId(Integer statusId) {
        return (root, query, criteriaBuilder) -> {
            Join<Ticket, TicketStatusClassificator> statusJoin = root.join("status");
            return criteriaBuilder.equal(statusJoin.get("id"), statusId);
        };
    }
    @Override
    public Predicate toPredicate(Root<Ticket> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        String likePattern = "%" + searchTerm.toLowerCase() + "%";
        Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern);
        Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern);
        Predicate workTypePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("workType")), likePattern);
        return criteriaBuilder.or(titlePredicate, descriptionPredicate, workTypePredicate);
    }

}
