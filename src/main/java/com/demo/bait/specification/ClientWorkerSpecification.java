package com.demo.bait.specification;

import com.demo.bait.entity.Client;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class ClientWorkerSpecification implements Specification<ClientWorker> {

    private String searchTerm;

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

    public static Specification<ClientWorker> isFavorite() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("favorite"), true);
    }

    @Override
    public Predicate toPredicate(Root<ClientWorker> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        String likePattern = "%" + searchTerm.toLowerCase().replaceAll("\\s+", "%") + "%";

        Predicate fullNamePredicate = criteriaBuilder.like(
                criteriaBuilder.concat(
                        criteriaBuilder.concat(criteriaBuilder.lower(root.get("firstName")), " "),
                        criteriaBuilder.lower(root.get("lastName"))
                ),
                likePattern
        );
        Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), likePattern);
        Predicate lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), likePattern);
        Predicate emailPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), likePattern);
        Predicate phoneNumberPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), likePattern);
        Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern);

        Join<ClientWorker, Location> locationJoin = root.join("location", JoinType.LEFT);
        Predicate locationPredicate = criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("name")), likePattern);

        Join<ClientWorker, Client> clientJoin = root.join("client", JoinType.LEFT);
        Predicate clientFullNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(clientJoin.get("fullName")), likePattern);
        Predicate clientShortNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(clientJoin.get("shortName")), likePattern);

        return criteriaBuilder.or(fullNamePredicate, firstNamePredicate, lastNamePredicate, emailPredicate,
                phoneNumberPredicate, titlePredicate, locationPredicate, clientFullNamePredicate,
                clientShortNamePredicate);
    }
}
