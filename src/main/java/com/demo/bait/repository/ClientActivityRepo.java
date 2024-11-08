package com.demo.bait.repository;

import com.demo.bait.entity.ClientActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientActivityRepo extends JpaRepository<ClientActivity, Integer> {

    List<ClientActivity> findByClientId(Integer clientId);
}
