package com.demo.bait.repository;

import com.demo.bait.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRepo extends JpaRepository<Maintenance, Integer> {
}
