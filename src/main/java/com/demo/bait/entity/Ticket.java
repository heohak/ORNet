package com.demo.bait.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
//    private Integer clientId;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
    private String description;

    @ManyToOne
    @JoinColumn(name = "main_ticket_id", referencedColumnName = "id")
    private Ticket ticket;
}
