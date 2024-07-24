package com.demo.bait.specification;

import com.demo.bait.entity.Client;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class ClientWorkerSpecification implements Specification<ClientWorker> {

    public static Specification<ClientWorker> hasClientId(Integer clientId) {
        return (root, query, criteriaBuilder) -> {
            Join<ClientWorker, Client> clientJoin = root.join("client");
            return criteriaBuilder.equal(clientJoin.get("id"), clientId);
        };
    }

    public static Specification<ClientWorker> hasRoleId(Integer roleId) {
        return (root, query, criteriaBuilder) -> {
            Join<ClientWorker, ClientWorkerRoleClassificator> rolesJoin = root.join("roles");
            return criteriaBuilder.equal(rolesJoin.get("id"), roleId);
        };
    }
    @Override
    public Predicate toPredicate(Root<ClientWorker> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
