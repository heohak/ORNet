package com.demo.bait.specification;

import com.demo.bait.entity.Client;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.ThirdPartyIT;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class ClientSpecification implements Specification<Client> {


    private String searchTerm;

    public static Specification<Client> hasClientType(String clientType) {
        return (root, query, criteriaBuilder) -> {
            switch (clientType) {
                case "pathology":
                    return criteriaBuilder.isTrue(root.get("pathologyClient"));
                case "surgery":
                    return criteriaBuilder.isTrue(root.get("surgeryClient"));
                case "editor":
                    return criteriaBuilder.isTrue(root.get("editorClient"));
                default:
                    return criteriaBuilder.conjunction();
            }
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
        Predicate workTypePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("otherMedicalInformation")), likePattern);

        Join<Client, Location> locationJoin = root.join("locations", JoinType.LEFT);
        Predicate locationPredicate = criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("name")), likePattern);

        Join<Client, ThirdPartyIT> thirdPartyITJoin = root.join("thirdPartyITs", JoinType.LEFT);
        Predicate thirdPartyITPredicate = criteriaBuilder.like(criteriaBuilder.lower(thirdPartyITJoin.get("name")), likePattern);

        return criteriaBuilder.or(titlePredicate, descriptionPredicate, workTypePredicate, locationPredicate, thirdPartyITPredicate);
    }
}
