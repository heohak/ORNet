package com.demo.bait.service.classificator;


import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.dto.classificator.TicketStatusClassificatorDTO;
import com.demo.bait.entity.ClientActivity;
import com.demo.bait.entity.Ticket;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.mapper.classificator.TicketStatusClassificatorMapper;
import com.demo.bait.repository.ClientActivityRepo;
import com.demo.bait.repository.TicketRepo;
import com.demo.bait.repository.classificator.TicketStatusClassificatorRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TicketStatusClassificatorService {

    private TicketStatusClassificatorRepo ticketStatusClassificatorRepo;
    private TicketStatusClassificatorMapper ticketStatusClassificatorMapper;
    private EntityManager entityManager;
    private TicketRepo ticketRepo;
    private ClientActivityRepo clientActivityRepo;


    @Transactional
    public ResponseDTO addTicketStatus(TicketStatusClassificatorDTO ticketStatusClassificatorDTO) {
        TicketStatusClassificator ticketStatus = new TicketStatusClassificator();
        ticketStatus.setStatus(ticketStatusClassificatorDTO.status());
        ticketStatus.setColor(ticketStatusClassificatorDTO.color());
        ticketStatusClassificatorRepo.save(ticketStatus);
        return new ResponseDTO("Ticket status classificator added successfully");
    }

    @Transactional
    public ResponseDTO updateTicketStatus(Integer statusId, TicketStatusClassificatorDTO statusDTO) {
        Optional<TicketStatusClassificator> ticketStatusOpt = ticketStatusClassificatorRepo.findById(statusId);
        if (ticketStatusOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket status classificator with id " + statusId + " not found");
        }
        TicketStatusClassificator ticketStatus = ticketStatusOpt.get();
        if (statusDTO.status() != null) {
            ticketStatus.setStatus(statusDTO.status());
        }
        if (statusDTO.color() != null) {
            ticketStatus.setColor(statusDTO.color());
        }
        ticketStatusClassificatorRepo.save(ticketStatus);
        return new ResponseDTO("Ticket status classificator updated successfully");
    }

    @Transactional
    public ResponseDTO deleteTicketStatus(Integer statusId) {
        Optional<TicketStatusClassificator> ticketStatusOpt = ticketStatusClassificatorRepo.findById(statusId);
        if (ticketStatusOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket status classificator with id " + statusId + " not found");
        }
        TicketStatusClassificator ticketStatus = ticketStatusOpt.get();

        List<Ticket> tickets = ticketRepo.findByStatus(ticketStatus);
        for (Ticket ticket : tickets) {
            ticket.setStatus(null);
            ticketRepo.save(ticket);
        }

        List<ClientActivity> clientActivities = clientActivityRepo.findByStatus(ticketStatus);
        for (ClientActivity clientActivity : clientActivities) {
            clientActivity.setStatus(null);
            clientActivityRepo.save(clientActivity);
        }

        ticketStatusClassificatorRepo.delete(ticketStatus);
        return new ResponseDTO("Ticket status classificator deleted successfully");
    }

    public List<TicketStatusClassificatorDTO> getAllTicketStatusClassificators() {
        return ticketStatusClassificatorMapper.toDtoList(ticketStatusClassificatorRepo.findAll());
    }

    public TicketStatusClassificatorDTO getTicketStatusClassificatorById(Integer ticketStatusId) {
        Optional<TicketStatusClassificator> ticketStatusOpt = ticketStatusClassificatorRepo.findById(ticketStatusId);
        if (ticketStatusOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket status classificator with id: " + ticketStatusId + " not found");
        }
        return ticketStatusClassificatorMapper.toDto(ticketStatusOpt.get());
    }

    public List<TicketStatusClassificatorDTO> getTicketStatusHistory(Integer statusId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(TicketStatusClassificator.class, statusId);

        List<TicketStatusClassificator> history = new ArrayList<>();
        for (Number rev : revisions) {
            TicketStatusClassificator status = auditReader.find(TicketStatusClassificator.class, statusId, rev);
            history.add(status);
        }
        return ticketStatusClassificatorMapper.toDtoList(history);
    }

    public List<TicketStatusClassificatorDTO> getDeletedTicketStatuses() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        AuditQuery query = auditReader.createQuery()
                .forRevisionsOfEntity(TicketStatusClassificator.class, false, true)
                .add(AuditEntity.revisionType().eq(RevisionType.DEL));

        List<Object[]> result = query.getResultList();

        List<TicketStatusClassificator> deletedEntities = result.stream()
                .map(r -> {
                    TicketStatusClassificator deletedEntity = (TicketStatusClassificator) r[0];
                    DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) r[1];

                    TicketStatusClassificator lastStateBeforeDeletion = auditReader.find(
                            TicketStatusClassificator.class,
                            deletedEntity.getId(),
                            revisionEntity.getId() - 1
                    );

                    return lastStateBeforeDeletion != null ? lastStateBeforeDeletion : deletedEntity;
                })
                .collect(Collectors.toList());
        return ticketStatusClassificatorMapper.toDtoList(deletedEntities);
    }
}
