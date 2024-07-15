package com.demo.bait.entity;

import com.demo.bait.converter.JsonConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class LinkedDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;
    private String name;
    private String manufacturer;
    private String productCode;
    private Integer serialNumber;
    private String comment;

    @Convert(converter = JsonConverter.class)
    private Map<String, Object> attributes;
}
