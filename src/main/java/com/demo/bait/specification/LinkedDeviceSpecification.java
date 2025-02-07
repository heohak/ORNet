package com.demo.bait.specification;

import com.demo.bait.entity.Client;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.LinkedDevice;
import com.demo.bait.entity.Location;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

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

    /**
     * Filters linked devices based on their introducedDate.
     *
     * @param date the reference date to compare against.
     * @param comparison "before" or "after"
     *                   if "before", returns devices with introducedDate less than the date and with the same date;
     *                   if "after", returns devices with introducedDate greater than the date and with the same date.
     * @return the specification predicate.
     */
    public static Specification<LinkedDevice> hasIntroducedDate(LocalDate date, String comparison) {
        return (Root<LinkedDevice> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (date == null || comparison == null || comparison.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            if ("before".equalsIgnoreCase(comparison)) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("introducedDate"), date);
            } else if ("after".equalsIgnoreCase(comparison)) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("introducedDate"), date);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<LinkedDevice> isNotUsed() {
        return (Root<LinkedDevice> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate deviceIsNull = criteriaBuilder.isNull(root.get("device"));
            Predicate templateIsFalseOrNull = criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("template"), false),
                    criteriaBuilder.isNull(root.get("template"))
            );
            return criteriaBuilder.and(deviceIsNull, templateIsFalseOrNull);
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
