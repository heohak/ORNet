package com.demo.bait.repository;

import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.ThirdPartyIT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThirdPartyITRepo extends JpaRepository<ThirdPartyIT, Integer> {

    List<ThirdPartyIT> findByFilesContaining(FileUpload file);
}
