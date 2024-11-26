package com.demo.bait.repository;

import com.demo.bait.entity.PredefinedDeviceName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredefinedDeviceNameRepo extends JpaRepository<PredefinedDeviceName, Integer> {
}
