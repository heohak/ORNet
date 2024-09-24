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

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TicketSpecificationService {

    private TicketRepo ticketRepo;
    private TicketMapper ticketMapper;

    public List<TicketDTO> searchAndFilterTickets(String searchTerm, Integer statusId, Boolean crisis, Boolean paid,
                                                  Integer workTypeId, Integer baitWorkerId) {
        Specification<Ticket> combinedSpec = Specification.where(null);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Specification<Ticket> searchSpec = new TicketSpecification(searchTerm);
            combinedSpec = combinedSpec.and(searchSpec);
        }

        if (statusId != null) {
            Specification<Ticket> statusSpec = TicketSpecification.hasStatusId(statusId);
            combinedSpec = combinedSpec.and(statusSpec);
        }

        if (crisis != null) {
            Specification<Ticket> crisisSpec = TicketSpecification.isCrisis(crisis);
            combinedSpec = combinedSpec.and(crisisSpec);
        }

        if (paid != null) {
            Specification<Ticket> paidSpec = TicketSpecification.isPaidWork(paid);
            combinedSpec = combinedSpec.and(paidSpec);
        }

        if (workTypeId != null) {
            Specification<Ticket> workTypeSpec = TicketSpecification.hasWorkTypeId(workTypeId);
            combinedSpec = combinedSpec.and(workTypeSpec);
        }

        if (baitWorkerId != null) {
            Specification<Ticket> baitWorkerSpec = TicketSpecification.hasBaitWorkerId(baitWorkerId);
            combinedSpec = combinedSpec.and(baitWorkerSpec);
        }

        return ticketMapper.toDtoList(ticketRepo.findAll(combinedSpec));
    }
}
