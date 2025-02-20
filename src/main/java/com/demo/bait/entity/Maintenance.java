package com.demo.bait.entity;

import com.demo.bait.converter.DurationConverter;
import com.demo.bait.enums.MaintenanceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Audited
@NoArgsConstructor
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String maintenanceName;
    private LocalDate maintenanceDate;
    private LocalDate lastDate;
    @Column(columnDefinition = "TEXT")
    private String comment;  // this is actually the description

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "maintenance_file_upload",
            joinColumns = @JoinColumn(name = "maintenance_id"),
            inverseJoinColumns = @JoinColumn(name = "file_upload_id")
    )
    private Set<FileUpload> files = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
    @Enumerated(EnumType.STRING)
    private MaintenanceStatus maintenanceStatus;
    @Convert(converter = DurationConverter.class)
    private Duration timeSpent;
    @ManyToOne
    @JoinColumn(name = "responsible_id", referencedColumnName = "id")
    private BaitWorker baitWorker;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "maintenance_device",
            joinColumns = @JoinColumn(name = "maintenance_id"),
            inverseJoinColumns = @JoinColumn(name = "device_id")
    )
    private Set<Device> devices = new HashSet<>();
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "maintenance_linked_device",
            joinColumns = @JoinColumn(name = "maintenance_id"),
            inverseJoinColumns = @JoinColumn(name = "linked_device_id")
    )
    private Set<LinkedDevice> linkedDevices = new HashSet<>();
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "maintenance_software",
            joinColumns = @JoinColumn(name = "maintenance_id"),
            inverseJoinColumns = @JoinColumn(name = "software_id")
    )
    private Set<Software> softwares = new HashSet<>();
    @Column(columnDefinition = "TEXT")
    private String internalComment;
}
