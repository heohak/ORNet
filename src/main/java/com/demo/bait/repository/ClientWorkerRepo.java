package com.demo.bait.repository;

import com.demo.bait.entity.Client;
import com.demo.bait.entity.ClientWorker;
import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientWorkerRepo extends JpaRepository<ClientWorker, Integer>, JpaSpecificationExecutor<ClientWorker> {

    List<ClientWorker> findByOrderByFavoriteDesc();
    List<ClientWorker> findByClientIdOrderByFavoriteDesc(Integer clientId);

    List<ClientWorker> findByRolesContaining(ClientWorkerRoleClassificator role);
    List<ClientWorker> findByClientIsNull();
    List<ClientWorker> findAllByClient(Client client);

}
