package com.demo.bait.repository;

import com.demo.bait.entity.Comment;
import com.demo.bait.entity.LinkedDevice;
import com.demo.bait.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LinkedDeviceRepo extends JpaRepository<LinkedDevice, Integer>, JpaSpecificationExecutor<LinkedDevice> {
    List<LinkedDevice> findByDeviceId(Integer deviceId);
    LinkedDevice findByCommentsContaining(Comment comment);
    List<LinkedDevice> findAllByLocation(Location location);
}
