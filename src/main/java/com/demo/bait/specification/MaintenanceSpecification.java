package com.demo.bait.specification;

import com.demo.bait.entity.*;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@AllArgsConstructor
public class MaintenanceSpecification implements Specification<Maintenance> {

    private String searchTerm;

    public static Specification<Maintenance> hasLocationId(Integer locationId) {
        return (Root<Maintenance> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (locationId != null) {
                return criteriaBuilder.equal(root.get("location").get("id"), locationId);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Maintenance> hasMaintenanceDate(LocalDate maintenanceDate) {
        return (Root<Maintenance> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (maintenanceDate != null) {
                return criteriaBuilder.equal(root.get("maintenanceDate"), maintenanceDate);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Maintenance> hasLastDate(LocalDate lastDate) {
        return (Root<Maintenance> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (lastDate != null) {
                return criteriaBuilder.equal(root.get("lastDate"), lastDate);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Maintenance> hasBaitWorkerId(Integer baitWorkerId) {
        return (Root<Maintenance> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (baitWorkerId != null) {
                return criteriaBuilder.equal(root.get("baitWorker").get("id"), baitWorkerId);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Maintenance> hasDeviceId(Integer deviceId) {
        return (Root<Maintenance> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (deviceId != null) {
                Join<Maintenance, Device> deviceJoin = root.join("devices");
                return criteriaBuilder.equal(deviceJoin.get("id"), deviceId);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Maintenance> hasLinkedDeviceId(Integer linkedDeviceId) {
        return (Root<Maintenance> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (linkedDeviceId != null) {
                Join<Maintenance, LinkedDevice> linkedDeviceJoin = root.join("linkedDevices");
                return criteriaBuilder.equal(linkedDeviceJoin.get("id"), linkedDeviceId);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Maintenance> hasSoftwareId(Integer softwareId) {
        return (Root<Maintenance> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (softwareId != null) {
                Join<Maintenance, Software> softwareJoin = root.join("softwares");
                return criteriaBuilder.equal(softwareJoin.get("id"), softwareId);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Maintenance> hasClientId(Integer clientId) {
        return (Root<Maintenance> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (clientId == null) {
                return criteriaBuilder.conjunction();
            }
            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<Client> clientRoot = subquery.from(Client.class);
            subquery.select(clientRoot.join("maintenances").get("id"))
                    .where(criteriaBuilder.equal(clientRoot.get("id"), clientId));
            return root.get("id").in(subquery);
        };
    }


    @Override
    public Predicate toPredicate(Root<Maintenance> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        String likePattern = "%" + searchTerm.toLowerCase() + "%";

        Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("maintenanceName")), likePattern);
        Predicate commentPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("comment")), likePattern);

        return criteriaBuilder.or(namePredicate, commentPredicate);
    }
}
