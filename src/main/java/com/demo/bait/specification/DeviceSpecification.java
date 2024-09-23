package com.demo.bait.specification;

import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.DeviceClassificator;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class DeviceSpecification implements Specification<Device> {

    private String searchTerm;

    public static Specification<Device> hasClassificatorId(Integer classificatorId) {
        return (root, query, criteriaBuilder) -> {
            Join<Device, DeviceClassificator> classificatorJoin = root.join("classificator");
            return criteriaBuilder.equal(classificatorJoin.get("id"), classificatorId);
        };
    }

    public static Specification<Device> hasClientId(Integer clientId) {
        return (root, query, criteriaBuilder) -> {
            Join<Device, Client> clientJoin = root.join("client");
            return criteriaBuilder.equal(clientJoin.get("id"), clientId);
        };
    }

    public static Specification<Device> hasLocationId(Integer locationId) {
        return (root, query, criteriaBuilder) -> {
            Join<Device, Location> locationJoin = root.join("location");
            return criteriaBuilder.equal(locationJoin.get("id"), locationId);
        };
    }

    public static Specification<Device> isWrittenOff(Boolean writtenOff) {
        return (root, query, criteriaBuilder) -> {
            if (writtenOff) {
                return criteriaBuilder.isNotNull(root.get("writtenOffDate"));
            }
            System.out.println("yeeeeeet");
            return criteriaBuilder.isNull(root.get("writtenOffDate"));
        };
    }

    @Override
    public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        String likePattern = "%" + searchTerm.toLowerCase() + "%";

        Predicate deviceNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("deviceName")), likePattern);
        Predicate departmentPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("department")), likePattern);
        Predicate roomPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("room")), likePattern);
        Predicate serialNumberPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("serialNumber")), likePattern);
        Predicate licenseNumberPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("licenseNumber")), likePattern);
        Predicate versionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("version")), likePattern);
        Predicate firstIPAddressPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstIPAddress")), likePattern);
        Predicate secondIPAddressPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("secondIPAddress")), likePattern);
        Predicate subnetMaskPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("subnetMask")), likePattern);
        Predicate softwareKeyPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("softwareKey")), likePattern);

        Join<Device, Client> clientJoin = root.join("client", JoinType.LEFT);
        Predicate clientFullNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(clientJoin.get("fullName")), likePattern);
        Predicate clientShortNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(clientJoin.get("shortName")), likePattern);

        Join<Device, Location> locationJoin = root.join("location", JoinType.LEFT);
        Predicate locationPredicate = criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("name")), likePattern);

        Join<Device, Comment> commentJoin = root.join("comments", JoinType.LEFT);
        Predicate commentPredicate = criteriaBuilder.like(criteriaBuilder.lower(commentJoin.get("comment")), likePattern);

        Join<Device, FileUpload> fileUploadJoin = root.join("files", JoinType.LEFT);
        Predicate fileUploadPredicate = criteriaBuilder.like(criteriaBuilder.lower(fileUploadJoin.get("fileName")), likePattern);

        Join<Device, Maintenance> maintenanceJoin = root.join("maintenances", JoinType.LEFT);
        Predicate maintenancePredicate = criteriaBuilder.like(criteriaBuilder.lower(maintenanceJoin.get("maintenanceName")), likePattern);

        return criteriaBuilder.or(
                deviceNamePredicate, departmentPredicate, roomPredicate, serialNumberPredicate, licenseNumberPredicate,
                versionPredicate, firstIPAddressPredicate, secondIPAddressPredicate, subnetMaskPredicate, softwareKeyPredicate,
                clientFullNamePredicate, clientShortNamePredicate, locationPredicate, commentPredicate, fileUploadPredicate,
                maintenancePredicate
        );
    }
}
