package com.demo.bait.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ClientWorker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String title;
//    private Integer clientId;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
}
