package com.demo.bait.repository;

import com.demo.bait.entity.Comment;
import com.demo.bait.entity.Location;
import com.demo.bait.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LocationRepo extends JpaRepository<Location, Integer>, JpaSpecificationExecutor<Location> {

//    @Query("SELECT l.maintenances FROM Location l WHERE l.id = :locationId AND EXISTS " +
//            "(SELECT m FROM l.maintenances m WHERE m.maintenanceDate BETWEEN :startDate AND :endDate)")
//    List<Maintenance> findMaintenancesByLocationAndDateRange(@Param("locationId") Integer locationId,
//                                                             @Param("startDate") LocalDate startDate,
//                                                             @Param("endDate") LocalDate endDate);

    Location findByCommentsContaining(Comment comment);
}
