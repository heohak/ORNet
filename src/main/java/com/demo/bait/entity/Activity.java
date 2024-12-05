package com.demo.bait.entity;

import com.demo.bait.converter.DurationConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Audited
@NoArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "TEXT")
    private String activity;
    private LocalDateTime timestamp;
    @Convert(converter = DurationConverter.class)
    private Duration timeSpent;
    private Boolean paid;
    private String username;
}
