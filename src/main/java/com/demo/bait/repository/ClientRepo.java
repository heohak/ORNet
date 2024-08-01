package com.demo.bait.repository;

import com.demo.bait.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ClientRepo extends JpaRepository<Client, Integer>, JpaSpecificationExecutor<Client> {

}
