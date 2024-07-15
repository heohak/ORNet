package com.demo.bait.entity;

import com.demo.bait.entity.classificator.TicketStatusClassificator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    private LocalDateTime startDateTime;
//    private LocalDateTime beenOpen;  // format p:h:min
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @ManyToMany
    @JoinTable(
            name = "ticket_client_worker",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "client_worker_id")
    )
    private Set<ClientWorker> contacts = new HashSet<>();

    private String workType;  // enum?? classificator?? piisab maintenance-ga sidumisest??
    private Boolean remote;
    private Boolean crisis;
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private TicketStatusClassificator status;
    @ManyToOne
    @JoinColumn(name = "responsible_id", referencedColumnName = "id")
    private BaitWorker baitWorker;
    private LocalDateTime responseDateTime;
    private String response;  // vastukirja sisu vastavalt vajadusele ehk vb vaja midagi muud kui string
    private String insideInfo;  // siseinfo mis ei lahe raportisse
    private LocalDateTime endDateTime;
    private String rootCause;

}
