package com.demo.bait.entity;

import com.demo.bait.components.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Audited
@NoArgsConstructor
public class Software {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
    private String name;
    private String dbVersion;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "vendorName", column = @Column(name = "his_vendor_name")),
            @AttributeOverride(name = "version", column = @Column(name = "his_version")),
            @AttributeOverride(name = "updateDate", column = @Column(name = "his_update_date"))
    })
    private HIS his;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "vendorName", column = @Column(name = "pacs_vendor_name")),
            @AttributeOverride(name = "version", column = @Column(name = "pacs_version")),
            @AttributeOverride(name = "updateDate", column = @Column(name = "pacs_update_date"))
    })
    private PACS pacs;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "vendorName", column = @Column(name = "dicom_vendor_name")),
            @AttributeOverride(name = "version", column = @Column(name = "dicom_version")),
            @AttributeOverride(name = "updateDate", column = @Column(name = "dicom_update_date"))
    })
    private DICOM dicom;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "vendorName", column = @Column(name = "hl7_vendor_name")),
            @AttributeOverride(name = "version", column = @Column(name = "hl7_version")),
            @AttributeOverride(name = "updateDate", column = @Column(name = "hl7_update_date"))
    })
    private HL7 hl7;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "vendorName", column = @Column(name = "lis_vendor_name")),
            @AttributeOverride(name = "version", column = @Column(name = "lis_version")),
            @AttributeOverride(name = "updateDate", column = @Column(name = "lis_update_date"))
    })
    private LIS lis;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "toReturn", column = @Column(name = "return_images")),
            @AttributeOverride(name = "link", column = @Column(name = "images_link")),
            @AttributeOverride(name = "updateDate", column = @Column(name = "images_update_date"))
    })
    private ReturnImagesToLIS returnImagesToLIS;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "version", column = @Column(name = "ORNetAPI_version")),
            @AttributeOverride(name = "updateDate", column = @Column(name = "ORNetAPI_update_date"))
    })
    private ORNetAPI orNetAPI;

    private LocalDate txtIntegrationDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "vendorName", column = @Column(name = "customerAPI_vendor_name")),
            @AttributeOverride(name = "version", column = @Column(name = "customerAPI_version")),
            @AttributeOverride(name = "updateDate", column = @Column(name = "customerAPI_update_date"))
    })
    private CustomerAPI customerAPI;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "version", column = @Column(name = "ORNetAPIClient_version")),
            @AttributeOverride(name = "updateDate", column = @Column(name = "ORNetAPIClient_update_date"))
    })
    private ORNetAPIClient orNetAPIClient;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "version", column = @Column(name = "consultationModule_version")),
            @AttributeOverride(name = "updateDate", column = @Column(name = "consultationModule_update_date"))
    })
    private ConsultationModule consultationModule;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "version", column = @Column(name = "AIModule_version")),
            @AttributeOverride(name = "updateDate", column = @Column(name = "AIModule_update_date"))
    })
    private AIModule aiModule;
}
