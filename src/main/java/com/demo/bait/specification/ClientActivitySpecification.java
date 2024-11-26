package com.demo.bait.specification;

import com.demo.bait.entity.Client;
import com.demo.bait.entity.ClientActivity;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class ClientActivitySpecification implements Specification<ClientActivity> {


    public static Specification<ClientActivity> hasStatusId(Integer statusId) {
        return (root, query, criteriaBuilder) -> {
            Join<ClientActivity, TicketStatusClassificator> statusJoin = root.join("status");
            return criteriaBuilder.equal(statusJoin.get("id"), statusId);
        };
    }

    public static Specification<ClientActivity> hasClientId(Integer clientId) {
        return (root, query, criteriaBuilder) -> {
            Join<ClientActivity, Client> clientJoin = root.join("client");
            return criteriaBuilder.equal(clientJoin.get("id"), clientId);
        };
    }

    @Override
    public Predicate toPredicate(Root<ClientActivity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
