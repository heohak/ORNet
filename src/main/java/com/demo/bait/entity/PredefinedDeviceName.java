package com.demo.bait.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PredefinedDeviceName {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_name_seq")
    @SequenceGenerator(name = "device_name_seq", sequenceName = "device_name_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;
}
