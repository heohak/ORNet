package com.demo.bait.repository;

import com.demo.bait.entity.ClientActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ClientActivityRepo extends JpaRepository<ClientActivity, Integer>,
        JpaSpecificationExecutor<ClientActivity> {

    List<ClientActivity> findByClientId(Integer clientId);
}
