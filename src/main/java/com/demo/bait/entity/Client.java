package com.demo.bait.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String thirdPartyIT;
    // locationi ja third party IT-d peab saama siduda mugavalt linnukesega
//    private String location;
//    private String locationAddress;

//    private String locationPhoneNumber;
//    private boolean pathologyClient;
//    private boolean surgeryClient;
//    private boolean editorClient;
//    private String otherMedicalInformation;

}
