package com.demo.bait.repository;

import com.demo.bait.entity.BaitWorker;
import com.demo.bait.entity.Client;
import com.demo.bait.entity.ClientActivity;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ClientActivityRepo extends JpaRepository<ClientActivity, Integer>,
        JpaSpecificationExecutor<ClientActivity> {

    List<ClientActivity> findByClientId(Integer clientId);

    List<ClientActivity> findByBaitWorker(BaitWorker baitWorker);
    List<ClientActivity> findByStatus(TicketStatusClassificator status);
    List<ClientActivity> findByWorkTypesContaining(WorkTypeClassificator workType);
    List<ClientActivity> findAllByClient(Client client);


}
