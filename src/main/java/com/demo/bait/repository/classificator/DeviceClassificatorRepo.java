package com.demo.bait.repository.classificator;

import com.demo.bait.entity.classificator.DeviceClassificator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceClassificatorRepo extends JpaRepository<DeviceClassificator, Integer> {
}
