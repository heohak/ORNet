package com.demo.bait.repository;

import com.demo.bait.entity.ClientWorker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientWorkerRepo extends JpaRepository<ClientWorker, Integer> {
}
