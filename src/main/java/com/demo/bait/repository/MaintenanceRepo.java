package com.demo.bait.repository;

import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRepo extends JpaRepository<Maintenance, Integer> {

    List<Maintenance> findByFilesContaining(FileUpload file);
}
