package com.demo.bait.repository;

import com.demo.bait.entity.Software;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoftwareRepo extends JpaRepository<Software, Integer> {
}
