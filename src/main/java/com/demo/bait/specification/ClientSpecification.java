package com.demo.bait.specification;

import com.demo.bait.entity.Client;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.ThirdPartyIT;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@AllArgsConstructor
public class ClientSpecification implements Specification<Client> {


    private String searchTerm;

    public static Specification<Client> hasClientTypes(List<String> clientTypes) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            for (String clientType : clientTypes) {
                switch (clientType.toLowerCase()) {
                    case "pathology" ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("pathologyClient")));
                    case "surgery" ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("surgeryClient")));
                    case "editor" ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("editorClient")));
                    case "other" ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("otherMedicalDevices")));
                    case "prospect" ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("prospect")));
                    case "agreement" ->
                            predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("agreement")));
                    default -> {
                    }
                }
            }

            return predicate;
        };
    }

    public static Specification<Client> hasLocationId(Integer locationId) {
        return (root, query, criteriaBuilder) -> {
            Join<Client, Location> locationJoin = root.join("locations");
            return criteriaBuilder.equal(locationJoin.get("id"), locationId);
        };
    }

    public static Specification<Client> hasThirdPartyId(Integer thirdPartyId) {
        return (root, query, criteriaBuilder) -> {
            Join<Client, ThirdPartyIT> thirdPartyITJoin = root.join("thirdPartyITs");
            return criteriaBuilder.equal(thirdPartyITJoin.get("id"), thirdPartyId);
        };
    }

    public static Specification<Client> hasCountry(String country) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("country")), country.toLowerCase());
    }

    public static Specification<Client> isActiveCustomer(Boolean isActive) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("activeCustomer"), isActive);
    }

    public static Specification<Client> hasLocationCountry(String country) {
        return (root, query, criteriaBuilder) -> {
            Join<Client, Location> locationJoin = root.join("locations", JoinType.LEFT);
            return criteriaBuilder.equal(criteriaBuilder.lower(locationJoin.get("country")), country.toLowerCase());
        };
    }

    @Override
    public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        String likePattern = "%" + searchTerm.toLowerCase() + "%";
        Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), likePattern);
        Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("shortName")), likePattern);

        Join<Client, Location> locationJoin = root.join("locations", JoinType.LEFT);
        Predicate locationPredicate = criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("name")), likePattern);

        Join<Client, ThirdPartyIT> thirdPartyITJoin = root.join("thirdPartyITs", JoinType.LEFT);
        Predicate thirdPartyITPredicate = criteriaBuilder.like(criteriaBuilder.lower(thirdPartyITJoin.get("name")), likePattern);

        return criteriaBuilder.or(titlePredicate, descriptionPredicate, locationPredicate, thirdPartyITPredicate);
    }
}
