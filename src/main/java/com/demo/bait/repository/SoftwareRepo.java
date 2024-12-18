package com.demo.bait.repository;

import com.demo.bait.entity.Client;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.entity.Software;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SoftwareRepo extends JpaRepository<Software, Integer> {
    List<Software> findByClientId(Integer clientId);
    List<Software> findByClientIsNull();
    List<Software> findAllByClient(Client client);

}
