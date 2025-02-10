package com.demo.bait.repository;

import com.demo.bait.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceCommentRepo extends JpaRepository<MaintenanceComment, Integer> {

    List<MaintenanceComment> findAllByMaintenance(Maintenance maintenance);
    List<MaintenanceComment> findByMaintenanceAndDevice(Maintenance maintenance, Device device);
    List<MaintenanceComment> findByMaintenanceAndLinkedDevice(Maintenance maintenance, LinkedDevice linkedDevice);
    List<MaintenanceComment> findByMaintenanceAndSoftware(Maintenance maintenance, Software software);
}
