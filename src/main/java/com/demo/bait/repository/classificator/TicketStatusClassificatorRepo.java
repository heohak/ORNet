package com.demo.bait.repository.classificator;

import com.demo.bait.entity.classificator.TicketStatusClassificator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketStatusClassificatorRepo extends JpaRepository<TicketStatusClassificator, Integer> {
}
