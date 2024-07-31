package com.demo.bait.repository;

import com.demo.bait.entity.Wiki;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WikiRepo extends JpaRepository<Wiki, Integer>, JpaSpecificationExecutor<Wiki> {
}
