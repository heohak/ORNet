package com.demo.bait.entity.classificator;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class TicketStatusClassificator {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_status_classificator_seq")
    @SequenceGenerator(name = "ticket_status_classificator_seq", sequenceName = "ticket_status_classificator_seq",
            allocationSize = 1)
    private Integer id;

    private String status;
}
