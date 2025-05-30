package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.TicketDTO;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.TicketMapper;
import com.demo.bait.repository.TicketRepo;
import com.demo.bait.specification.TicketSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TicketSpecificationService {

    private TicketRepo ticketRepo;
    private TicketMapper ticketMapper;

    public List<TicketDTO> searchAndFilterTickets(String searchTerm, Integer statusId, Boolean crisis, Boolean paid,
                                                  Integer workTypeId, Integer baitWorkerId) {
        log.info("Starting ticket search and filter process");

        Specification<Ticket> combinedSpec = Specification.where(null);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            log.debug("Adding search term specification: {}", searchTerm);
            Specification<Ticket> searchSpec = new TicketSpecification(searchTerm);
            combinedSpec = combinedSpec.and(searchSpec);
        }

        if (statusId != null) {
            log.debug("Adding status ID specification: {}", statusId);
            Specification<Ticket> statusSpec = TicketSpecification.hasStatusId(statusId);
            combinedSpec = combinedSpec.and(statusSpec);
        }

        if (crisis != null) {
            log.debug("Adding crisis specification: {}", crisis);
            Specification<Ticket> crisisSpec = TicketSpecification.isCrisis(crisis);
            combinedSpec = combinedSpec.and(crisisSpec);
        }

        if (paid != null) {
            log.debug("Adding paid specification: {}", paid);
            Specification<Ticket> paidSpec = TicketSpecification.isPaid(paid);
            combinedSpec = combinedSpec.and(paidSpec);
        }

        if (workTypeId != null) {
            log.debug("Adding work type ID specification: {}", workTypeId);
            Specification<Ticket> workTypeSpec = TicketSpecification.hasWorkTypeId(workTypeId);
            combinedSpec = combinedSpec.and(workTypeSpec);
        }

        if (baitWorkerId != null) {
            log.debug("Adding bait worker ID specification: {}", baitWorkerId);
            Specification<Ticket> baitWorkerSpec = TicketSpecification.hasBaitWorkerId(baitWorkerId);
            combinedSpec = combinedSpec.and(baitWorkerSpec);
        }

        log.info("Fetching filtered tickets from the database");
        List<TicketDTO> ticketDTOs = ticketMapper.toDtoList(ticketRepo.findAll(combinedSpec));

        log.info("Sorting filtered tickets by title");
        ticketDTOs.sort(Comparator.comparing(
                ticketDTO -> ticketDTO.title() != null ? ticketDTO.title() : "",
                String::compareToIgnoreCase
        ));

        log.info("Ticket search and filter process completed. Found {} tickets.", ticketDTOs.size());
        return ticketDTOs;
    }
}
