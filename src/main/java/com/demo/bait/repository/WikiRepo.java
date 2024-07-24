package com.demo.bait.repository;

import com.demo.bait.entity.Wiki;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WikiRepo extends JpaRepository<Wiki, Integer> {
}
