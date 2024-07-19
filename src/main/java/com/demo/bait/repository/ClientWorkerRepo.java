package com.demo.bait.repository;

import com.demo.bait.entity.ClientWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientWorkerRepo extends JpaRepository<ClientWorker, Integer>, JpaSpecificationExecutor<ClientWorker> {

    List<ClientWorker> findByClientId(Integer clientId);
    List<ClientWorker> findByLocationId(Integer locationId);
    @Query("SELECT cw FROM ClientWorker cw JOIN cw.roles r WHERE r.id = :roleId")
    List<ClientWorker> findByRoleId(@Param("roleId") Integer roleId);
}
