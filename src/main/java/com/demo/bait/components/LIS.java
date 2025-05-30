package com.demo.bait.components;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class LIS {
    private String vendorName;
    private String version;
    private LocalDate updateDate;
}
