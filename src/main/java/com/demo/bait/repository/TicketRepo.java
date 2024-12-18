package com.demo.bait.repository;

import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket, Integer>, JpaSpecificationExecutor<Ticket> {

    List<Ticket> findByClientId(Integer clientId);
    Ticket findByActivitiesContaining(Activity activity);
    List<Ticket> findByDevicesContains(Device device);

    @Query("SELECT t FROM Ticket t WHERE t.client.id = :clientId AND t.startDateTime BETWEEN :startDateTime AND :endDateTime")
    List<Ticket> findByClientAndDateRange(
            @Param("clientId") Integer clientId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);

    List<Ticket> findByBaitWorker(BaitWorker baitWorker);
    List<Ticket> findByStatus(TicketStatusClassificator status);
    List<Ticket> findByWorkTypesContaining(WorkTypeClassificator workType);
    List<Ticket> findAllByContactsContaining(ClientWorker worker);
    List<Ticket> findAllByClient(Client client);


}
