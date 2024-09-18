package com.demo.bait.entity;

import com.demo.bait.entity.classificator.ClientWorkerRoleClassificator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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
public class ClientWorker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    @Pattern(regexp = "^\\+?[0-9 ]{1,15}$", message = "Invalid phone number format")
    private String phoneNumber;
    private String title;
//    private Integer clientId;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @ManyToMany
    @JoinTable(
            name = "client_worker_role",
            joinColumns = @JoinColumn(name = "client_worker_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<ClientWorkerRoleClassificator> roles = new HashSet<>();
    private Boolean favorite;
}
