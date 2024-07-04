package com.demo.bait.entity;

import com.demo.bait.components.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Software {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
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
}
