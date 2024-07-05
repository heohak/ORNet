package com.demo.bait.repository;

import com.demo.bait.entity.LinkedDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkedDeviceRepo extends JpaRepository<LinkedDevice, Integer> {
    List<LinkedDevice> findByDeviceId(Integer deviceId);
}
