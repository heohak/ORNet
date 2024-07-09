package com.demo.bait.repository;

import com.demo.bait.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepo extends JpaRepository<Location, Integer> {
}
