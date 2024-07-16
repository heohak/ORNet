package com.demo.bait.entity.classificator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ClientWorkerRoleClassificator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String role;
}
