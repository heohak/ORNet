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
        log.info("Adding new Ticket Status Classificator: {}", ticketStatusClassificatorDTO);
        try {
            TicketStatusClassificator ticketStatus = new TicketStatusClassificator();
            ticketStatus.setStatus(ticketStatusClassificatorDTO.status());
            ticketStatus.setColor(ticketStatusClassificatorDTO.color());
            ticketStatusClassificatorRepo.save(ticketStatus);
            log.info("Successfully added Ticket Status Classificator: {}", ticketStatus);
            return new ResponseDTO("Ticket status classificator added successfully");
        } catch (Exception e) {
            log.error("Error while adding Ticket Status Classificator: {}", ticketStatusClassificatorDTO, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO updateTicketStatus(Integer statusId, TicketStatusClassificatorDTO statusDTO) {
        log.info("Updating Ticket Status Classificator with ID: {}", statusId);
        try {
            Optional<TicketStatusClassificator> ticketStatusOpt = ticketStatusClassificatorRepo.findById(statusId);
            if (ticketStatusOpt.isEmpty()) {
                log.warn("Ticket Status Classificator with ID {} not found", statusId);
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
            log.info("Successfully updated Ticket Status Classificator with ID: {}", statusId);
            return new ResponseDTO("Ticket status classificator updated successfully");
        } catch (Exception e) {
            log.error("Error while updating Ticket Status Classificator with ID: {}", statusId, e);
            throw e;
        }
    }

    @Transactional
    public ResponseDTO deleteTicketStatus(Integer statusId) {
        log.info("Deleting Ticket Status Classificator with ID: {}", statusId);
        try {
            Optional<TicketStatusClassificator> ticketStatusOpt = ticketStatusClassificatorRepo.findById(statusId);
            if (ticketStatusOpt.isEmpty()) {
                log.warn("Ticket Status Classificator with ID {} not found", statusId);
                throw new EntityNotFoundException("Ticket status classificator with id " + statusId + " not found");
            }
            TicketStatusClassificator ticketStatus = ticketStatusOpt.get();

            log.debug("Unlinking associated Tickets for Status ID: {}", statusId);
            List<Ticket> tickets = ticketRepo.findByStatus(ticketStatus);
            for (Ticket ticket : tickets) {
                ticket.setStatus(null);
                ticketRepo.save(ticket);
            }

            log.debug("Unlinking associated Client Activities for Status ID: {}", statusId);
            List<ClientActivity> clientActivities = clientActivityRepo.findByStatus(ticketStatus);
            for (ClientActivity clientActivity : clientActivities) {
                clientActivity.setStatus(null);
                clientActivityRepo.save(clientActivity);
            }

            ticketStatusClassificatorRepo.delete(ticketStatus);
            log.info("Successfully deleted Ticket Status Classificator with ID: {}", statusId);
            return new ResponseDTO("Ticket status classificator deleted successfully");
        } catch (Exception e) {
            log.error("Error while deleting Ticket Status Classificator with ID: {}", statusId, e);
            throw e;
        }
    }

    public List<TicketStatusClassificatorDTO> getAllTicketStatusClassificators() {
        log.info("Fetching all Ticket Status Classificators");
        try {
            List<TicketStatusClassificatorDTO> result = ticketStatusClassificatorMapper.toDtoList(ticketStatusClassificatorRepo.findAll());
            log.debug("Fetched Ticket Status Classificators: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error while fetching all Ticket Status Classificators", e);
            throw e;
        }
    }

    public TicketStatusClassificatorDTO getTicketStatusClassificatorById(Integer ticketStatusId) {
        log.info("Fetching Ticket Status Classificator with ID: {}", ticketStatusId);
        try {
            Optional<TicketStatusClassificator> ticketStatusOpt = ticketStatusClassificatorRepo.findById(ticketStatusId);
            if (ticketStatusOpt.isEmpty()) {
                log.warn("Ticket Status Classificator with ID {} not found", ticketStatusId);
                throw new EntityNotFoundException("Ticket status classificator with id: " + ticketStatusId + " not found");
            }
            TicketStatusClassificatorDTO result = ticketStatusClassificatorMapper.toDto(ticketStatusOpt.get());
            log.debug("Fetched Ticket Status Classificator with ID {}: {}", ticketStatusId, result);
            return result;
        } catch (Exception e) {
            log.error("Error while fetching Ticket Status Classificator with ID: {}", ticketStatusId, e);
            throw e;
        }
    }

    public List<TicketStatusClassificatorDTO> getTicketStatusHistory(Integer statusId) {
        log.info("Fetching history for Ticket Status Classificator with ID: {}", statusId);
        try {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);
            List<Number> revisions = auditReader.getRevisions(TicketStatusClassificator.class, statusId);

            List<TicketStatusClassificator> history = new ArrayList<>();
            for (Number rev : revisions) {
                TicketStatusClassificator status = auditReader.find(TicketStatusClassificator.class, statusId, rev);
                history.add(status);
            }
            List<TicketStatusClassificatorDTO> result = ticketStatusClassificatorMapper.toDtoList(history);
            log.debug("Fetched history for Ticket Status Classificator with ID {}: {}", statusId, result);
            return result;
        } catch (Exception e) {
            log.error("Error while fetching history for Ticket Status Classificator with ID: {}", statusId, e);
            throw e;
        }
    }

    public List<TicketStatusClassificatorDTO> getDeletedTicketStatuses() {
        log.info("Fetching deleted Ticket Status Classificators");
        try {
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

            List<TicketStatusClassificatorDTO> deletedDTOs = ticketStatusClassificatorMapper.toDtoList(deletedEntities);
            log.debug("Fetched deleted Ticket Status Classificators: {}", deletedDTOs);
            return deletedDTOs;
        } catch (Exception e) {
            log.error("Error while fetching deleted Ticket Status Classificators", e);
            throw e;
        }
    }
}
