package com.demo.bait.specification;

import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;


@AllArgsConstructor
public class TicketSpecification implements Specification<Ticket> {

    private String searchTerm;

    public static Specification<Ticket> hasStatusId(Integer statusId) {
        return (root, query, criteriaBuilder) -> {
            Join<Ticket, TicketStatusClassificator> statusJoin = root.join("status");
            return criteriaBuilder.equal(statusJoin.get("id"), statusId);
        };
    }

    public static Specification<Ticket> hasWorkTypeId(Integer workTypeId) {
        return (root, query, criteriaBuilder) -> {
            Join<Ticket, WorkTypeClassificator> workTypeJoin = root.join("workTypes", JoinType.LEFT);
            return criteriaBuilder.equal(workTypeJoin.get("id"), workTypeId);
        };
    }

    public static Specification<Ticket> hasBaitWorkerId(Integer baitWorkerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("baitWorker").get("id"), baitWorkerId);
    }

    public static Specification<Ticket> isCrisis(Boolean crisis) {
        return (root, query, criteriaBuilder) -> {
            if (crisis == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("crisis"), crisis);
        };
    }

    public static Specification<Ticket> isPaid(Boolean paid) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("paid"), paid);
        };
    }

    @Override
    public Predicate toPredicate(Root<Ticket> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        String likePattern = "%" + searchTerm.toLowerCase() + "%";
        Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern);
        Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern);
        Predicate baitNumerationPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("baitNumeration")), likePattern);
        Predicate clientNumerationPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("clientNumeration")), likePattern);
        Predicate responsePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("response")), likePattern);
        Predicate insideInfoPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("insideInfo")), likePattern);
        Predicate rootCausePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("rootCause")), likePattern);

        Join<Ticket, Client> clientJoin = root.join("client", JoinType.LEFT);
        Predicate clientFullNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(clientJoin.get("fullName")), likePattern);
        Predicate clientShortNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(clientJoin.get("shortName")), likePattern);

        Join<Ticket, Location> locationJoin = root.join("location", JoinType.LEFT);
        Predicate locationPredicate = criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("name")), likePattern);

        Join<Ticket, ClientWorker> contactsJoin = root.join("contacts", JoinType.LEFT);
        Predicate contactsFirstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(contactsJoin.get("firstName")), likePattern);
        Predicate contactsLastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(contactsJoin.get("lastName")), likePattern);
        Predicate contactsTitlePredicate = criteriaBuilder.like(criteriaBuilder.lower(contactsJoin.get("title")), likePattern);

        Join<Ticket, BaitWorker> baitWorkerJoin = root.join("baitWorker", JoinType.LEFT);
        Predicate baitWorkerFirstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(baitWorkerJoin.get("firstName")), likePattern);
        Predicate baitWorkerLastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(baitWorkerJoin.get("lastName")), likePattern);

        Join<Ticket, Comment> commentJoin = root.join("comments", JoinType.LEFT);
        Predicate commentPredicate = criteriaBuilder.like(criteriaBuilder.lower(commentJoin.get("comment")), likePattern);

        Join<Ticket, Maintenance> maintenanceJoin = root.join("maintenances", JoinType.LEFT);
        Predicate maintenancePredicate = criteriaBuilder.like(criteriaBuilder.lower(maintenanceJoin.get("maintenanceName")), likePattern);

        Join<Ticket, FileUpload> fileUploadJoin = root.join("files", JoinType.LEFT);
        Predicate fileUploadPredicate = criteriaBuilder.like(criteriaBuilder.lower(fileUploadJoin.get("fileName")), likePattern);

        return criteriaBuilder.or(titlePredicate, descriptionPredicate, baitNumerationPredicate,
                clientNumerationPredicate, responsePredicate, insideInfoPredicate, rootCausePredicate,
                clientFullNamePredicate, clientShortNamePredicate, locationPredicate, contactsFirstNamePredicate,
                contactsLastNamePredicate, contactsTitlePredicate, baitWorkerFirstNamePredicate,
                baitWorkerLastNamePredicate, commentPredicate, maintenancePredicate, fileUploadPredicate);
    }

}
