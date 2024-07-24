package com.demo.bait.specification;

import com.demo.bait.entity.Client;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.classificator.DeviceClassificator;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class DeviceSpecification implements Specification<Device>{

    private String searchTerm;

    public static Specification<Device> hasClassificatorId(Integer deviceId) {
        return (root, query, criteriaBuilder) -> {
            Join<Device, DeviceClassificator> classificatorJoin = root.join("classificator");
            return criteriaBuilder.equal(classificatorJoin.get("id"), deviceId);
        };
    }

    public static Specification<Device> hasClientId(Integer deviceId) {
        return (root, query, criteriaBuilder) -> {
            Join<Device, Client> clientJoin = root.join("client");
            return criteriaBuilder.equal(clientJoin.get("id"), deviceId);
        };
    }

    @Override
    public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        String likePattern = "%" + searchTerm.toLowerCase() + "%";
        Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("deviceName")), likePattern);
        Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("department")), likePattern);
        Predicate workTypePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("comment")), likePattern);
        return criteriaBuilder.or(titlePredicate, descriptionPredicate, workTypePredicate);
    }
}
