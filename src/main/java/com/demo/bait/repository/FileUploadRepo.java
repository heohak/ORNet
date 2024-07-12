package com.demo.bait.repository;

import com.demo.bait.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileUploadRepo extends JpaRepository<FileUpload, Integer> {
}
