package com.demo.bait.specification;

import com.demo.bait.entity.Client;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.LinkedDevice;
import com.demo.bait.entity.Location;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class LinkedDeviceSpecification implements Specification<LinkedDevice> {

    private String searchTerm;

    public static Specification<LinkedDevice> hasLocationId(Integer locationId) {
        return (root, query, criteriaBuilder) -> {
            Join<LinkedDevice, Location> locationJoin = root.join("location");
            return criteriaBuilder.equal(locationJoin.get("id"), locationId);
        };
    }

    public static Specification<LinkedDevice> hasDeviceId(Integer deviceId) {
        return (root, query, criteriaBuilder) -> {
            Join<LinkedDevice, Device> clientJoin = root.join("device");
            return criteriaBuilder.equal(clientJoin.get("id"), deviceId);
        };
    }

    public static Specification<LinkedDevice> isTemplate(Boolean template) {
        return (root, query, criteriaBuilder) -> {
            if (template != null) {
                return criteriaBuilder.equal(root.get("template"), template);
            }
            return criteriaBuilder.conjunction();
        };
    }

    @Override
    public Predicate toPredicate(Root<LinkedDevice> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        String likePattern = "%" + searchTerm.toLowerCase() + "%";

        Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern);
        Predicate manufacturerPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("manufacturer")), likePattern);
        Predicate productCodePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("productCode")), likePattern);
        Predicate serialNumberPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("serialNumber")), likePattern);

        return criteriaBuilder.or(
                namePredicate, manufacturerPredicate, productCodePredicate, serialNumberPredicate
        );
    }
}
