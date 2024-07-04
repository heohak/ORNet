package com.demo.bait.repository;

import com.demo.bait.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket, Integer> {

    List<Ticket> findByClientId(Integer clientId);
    List<Ticket> findByTicketId(Integer mainTicketId);
}
