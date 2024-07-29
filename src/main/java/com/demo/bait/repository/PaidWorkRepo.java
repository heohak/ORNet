package com.demo.bait.repository;

import com.demo.bait.entity.PaidWork;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaidWorkRepo extends JpaRepository<PaidWork, Integer> {
}
