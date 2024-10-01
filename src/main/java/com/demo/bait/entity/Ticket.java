package com.demo.bait.entity;

import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Audited
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq")
    @SequenceGenerator(name = "ticket_seq", sequenceName = "ticket_seq", allocationSize = 1)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
    private String title;
    private String name;
    private String baitNumeration;
    private String clientNumeration;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime startDateTime;
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
    @Column(columnDefinition = "TEXT")
    private String response;  // vastukirja sisu vastavalt vajadusele ehk vb vaja midagi muud kui string
    @Column(columnDefinition = "TEXT")
    private String insideInfo;  // siseinfo mis ei lahe raportisse (Internal comment)
    private LocalDateTime endDateTime;
    private LocalDateTime updateDateTime;
    @Column(columnDefinition = "TEXT")
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
            name = "ticket_file_upload",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "file_upload_id")
    )
    private Set<FileUpload> files = new HashSet<>();
    @OneToOne
    @JoinColumn(name = "paid_work_id", referencedColumnName = "id")
    private PaidWork paidWork;
}
