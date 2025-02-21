package com.demo.bait.repository;

import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MaintenanceRepo extends JpaRepository<Maintenance, Integer>, JpaSpecificationExecutor<Maintenance> {

    List<Maintenance> findByFilesContaining(FileUpload file);
    List<Maintenance> findAllByLocation(Location location);
}
