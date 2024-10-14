package com.demo.bait.repository;

import com.demo.bait.entity.Activity;
import com.demo.bait.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket, Integer>, JpaSpecificationExecutor<Ticket> {

    List<Ticket> findByClientId(Integer clientId);
//    List<Ticket> findByTicketId(Integer mainTicketId);
    List<Ticket> findByStatusId(Integer statusId);
    Ticket findByActivitiesContaining(Activity activity);

    @Query("SELECT t FROM Ticket t WHERE t.client.id = :clientId AND t.startDateTime BETWEEN :startDateTime AND :endDateTime")
    List<Ticket> findByClientAndDateRange(
            @Param("clientId") Integer clientId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
}
