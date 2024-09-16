package com.demo.bait.repository;

import com.demo.bait.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LocationRepo extends JpaRepository<Location, Integer>, JpaSpecificationExecutor<Location> {
}
