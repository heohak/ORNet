package com.demo.bait.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
//    private Integer clientId;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
    private String deviceName; // vb tuleks teha enum-iga et saaks lihtsamalt yldistusi teha
    private String department;
    private String room;
    private Integer serialNumber;
    private String licenseNumber;
    private String version;
    private LocalDate versionUpdateDate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "device_maintenance",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "maintenance_id")
    )
    private Set<Maintenance> maintenances = new HashSet<>();

    private String firstIPAddress;
    private String secondIPAddress;
    private String softwareKey; // kui null siis ei ole olemas seda ehk ei ole vaja
    private LocalDate introducedDate;
    private LocalDate writtenOffDate;
    private String comment; // vaba + vajaduesl failid
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "device_file_upload",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "file_upload_id")
    )
    private Set<FileUpload> files = new HashSet<>();

}
