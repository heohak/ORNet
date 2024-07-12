package com.demo.bait.repository;

import com.demo.bait.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepo extends JpaRepository<Device, Integer> {

    List<Device> findByClientId(Integer clientId);
    List<Device> findByClassificatorId(Integer classificatorId);
}
