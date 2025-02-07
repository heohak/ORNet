package com.demo.bait.repository;

import com.demo.bait.entity.*;
import com.demo.bait.entity.classificator.DeviceClassificator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DeviceRepo extends JpaRepository<Device, Integer>, JpaSpecificationExecutor<Device> {

    List<Device> findByClientId(Integer clientId);
    List<Device> findByClassificatorId(Integer classificatorId);

//    @Query("SELECT d.maintenances FROM Device d WHERE d.id = :deviceId AND EXISTS " +
//            "(SELECT m FROM d.maintenances m WHERE m.maintenanceDate BETWEEN :startDate AND :endDate)")
//    List<Maintenance> findMaintenancesByDeviceAndDateRange(@Param("deviceId") Integer deviceId,
//                                                           @Param("startDate") LocalDate startDate,
//                                                           @Param("endDate") LocalDate endDate);

    List<Device> findByClassificator(DeviceClassificator classificator);
    Device findByCommentsContaining(Comment comment);
    List<Device> findAllByClient(Client client);
    List<Device> findAllByLocation(Location location);
    List<Device> findByFilesContaining(FileUpload file);
}
