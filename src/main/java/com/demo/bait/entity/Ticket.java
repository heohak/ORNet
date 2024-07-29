package com.demo.bait.entity;

import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
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
    private String title;
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

//    private String workType;  // enum?? classificator?? piisab maintenance-ga sidumisest??
    @ManyToMany
    @JoinTable(
            name = "ticket_work_type_classificator",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "work_type_id")
    )
    private Set<WorkTypeClassificator> workTypes = new HashSet<>();
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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "ticket_comment",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "ticket_maintenance",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "maintenance_id")
    )
    private Set<Maintenance> maintenances = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "ticket_file_upload",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "file_upload_id")
    )
    private Set<FileUpload> files = new HashSet<>();
}
