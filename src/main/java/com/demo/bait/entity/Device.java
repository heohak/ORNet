package com.demo.bait.entity;

import com.demo.bait.converter.JsonConverter;
import com.demo.bait.entity.classificator.DeviceClassificator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Audited
@NoArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    private Integer clientId;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
    private String deviceName;
    @ManyToOne
    @JoinColumn(name = "classificator_id")
    private DeviceClassificator classificator;
    private String department;
    private String room;
    private String serialNumber;
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
    private String subnetMask;
    private String softwareKey; // kui null siis ei ole olemas seda ehk ei ole vaja
    private LocalDate introducedDate;
    private LocalDate writtenOffDate;
//    private String comment; // vaba + vajaduesl failid
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "device_comment",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "device_file_upload",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "file_upload_id")
    )
    private Set<FileUpload> files = new HashSet<>();

    @Convert(converter = JsonConverter.class)
    private Map<String, Object> attributes;

}
