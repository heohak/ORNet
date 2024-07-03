package com.demo.bait.repository;

import com.demo.bait.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepo extends JpaRepository<Ticket, Integer> {
}
