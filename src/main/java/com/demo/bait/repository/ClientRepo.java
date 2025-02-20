package com.demo.bait.repository;

import com.demo.bait.entity.Client;
import com.demo.bait.entity.FileUpload;
import com.demo.bait.entity.Maintenance;
import com.demo.bait.entity.ThirdPartyIT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ClientRepo extends JpaRepository<Client, Integer>, JpaSpecificationExecutor<Client> {

    List<Client> findByThirdPartyITsContaining(ThirdPartyIT thirdPartyIT);
    List<Client> findByContractTerms(FileUpload file);

    @Query("SELECT c.maintenances FROM Client c WHERE c.id = :clientId AND EXISTS " +
            "(SELECT m FROM c.maintenances m WHERE m.maintenanceDate BETWEEN :startDate AND :endDate)")
    List<Maintenance> findMaintenancesByClientAndDateRange(@Param("clientId") Integer clientId,
                                                           @Param("startDate") LocalDate startDate,
                                                           @Param("endDate") LocalDate endDate);
    List<Client> findAllByMaintenancesContaining(Maintenance maintenance);
}
