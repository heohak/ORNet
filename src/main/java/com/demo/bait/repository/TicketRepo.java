package com.demo.bait.repository;

import com.demo.bait.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket, Integer>, JpaSpecificationExecutor<Ticket> {

    List<Ticket> findByClientId(Integer clientId);
//    List<Ticket> findByTicketId(Integer mainTicketId);
    List<Ticket> findByStatusId(Integer statusId);
}
