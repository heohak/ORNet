package com.demo.bait.entity;

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
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String fullName;
    private String shortName;
//    private String thirdPartyIT;
    // locationi ja third party IT-d peab saama siduda mugavalt linnukesega

    @ManyToMany
    @JoinTable(
            name = "client_location",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> locations = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "client_third_party_IT",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "third_party_IT_id")
    )
    private Set<ThirdPartyIT> thirdPartyITs = new HashSet<>();

    private Boolean pathologyClient;
    private Boolean surgeryClient;
    private Boolean editorClient;
    private String otherMedicalInformation;
    private LocalDateTime lastMaintenance;
    private LocalDateTime nextMaintenance;

}
