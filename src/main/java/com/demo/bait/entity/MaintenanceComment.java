package com.demo.bait.entity;

import com.demo.bait.enums.MaintenanceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Audited
@NoArgsConstructor
public class MaintenanceComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "maintenance_id", referencedColumnName = "id")
    private Maintenance maintenance;
    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;
    @ManyToOne
    @JoinColumn(name = "linked_device_id", referencedColumnName = "id")
    private LinkedDevice linkedDevice;
    @ManyToOne
    @JoinColumn(name = "software_id", referencedColumnName = "id")
    private Software software;
    @Column(columnDefinition = "TEXT")
    private String comment;
    @Enumerated(EnumType.STRING)
    private MaintenanceStatus maintenanceStatus;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "maintenance_comment_file_upload",
            joinColumns = @JoinColumn(name = "maintenance_comment_id"),
            inverseJoinColumns = @JoinColumn(name = "file_upload_id")
    )
    private Set<FileUpload> files = new HashSet<>();
}
