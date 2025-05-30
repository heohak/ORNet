package com.demo.bait.entity;

import com.demo.bait.converter.DurationConverter;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.entity.classificator.WorkTypeClassificator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Audited
@NoArgsConstructor
public class ClientActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
    private String title;
//    private String clientNumeration;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime startDateTime;
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
    @ManyToMany
    @JoinTable(
            name = "client_activity_client_worker",
            joinColumns = @JoinColumn(name = "client_activity_id"),
            inverseJoinColumns = @JoinColumn(name = "client_worker_id")
    )
    private Set<ClientWorker> contacts = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "client_activity_work_type_classificator",
            joinColumns = @JoinColumn(name = "client_activity_id"),
            inverseJoinColumns = @JoinColumn(name = "work_type_id")
    )
    private Set<WorkTypeClassificator> workTypes = new HashSet<>();
    private Boolean crisis;
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private TicketStatusClassificator status;
    @ManyToOne
    @JoinColumn(name = "responsible_id", referencedColumnName = "id")
    private BaitWorker baitWorker;
    private LocalDateTime endDateTime;
    private LocalDateTime updateDateTime;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "client_activity_file_upload",
            joinColumns = @JoinColumn(name = "client_activity_id"),
            inverseJoinColumns = @JoinColumn(name = "file_upload_id")
    )
    private Set<FileUpload> files = new HashSet<>();
//    private Boolean paid;
//    private Boolean settled;
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "client_activity_device",
//            joinColumns = @JoinColumn(name = "client_activity_id"),
//            inverseJoinColumns = @JoinColumn(name = "device_id")
//    )
//    private Set<Device> devices = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "client_activity_comment",
            joinColumns = @JoinColumn(name = "client_activity_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    private Set<Activity> activities = new HashSet<>();
}
