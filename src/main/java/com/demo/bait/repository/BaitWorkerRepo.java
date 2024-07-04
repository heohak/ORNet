package com.demo.bait.repository;

import com.demo.bait.entity.BaitWorker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaitWorkerRepo extends JpaRepository<BaitWorker, Integer> {
}
