package com.demo.bait.repository;

import com.demo.bait.entity.ClientWorker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientWorkerRepo extends JpaRepository<ClientWorker, Integer> {

    List<ClientWorker> findByClientId(Integer clientId);
    List<ClientWorker> findByLocationId(Integer locationId);
}
