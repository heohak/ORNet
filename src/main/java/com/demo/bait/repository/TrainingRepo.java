package com.demo.bait.repository;

import com.demo.bait.entity.Client;
import com.demo.bait.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrainingRepo extends JpaRepository<Training, Integer>, JpaSpecificationExecutor<Training> {

    List<Training> findAllByClient(Client client);
}
