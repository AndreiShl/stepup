package ru.inno.task4.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Table(name = "stepup_logins")
@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Instant access_date;
    @ManyToOne
    private User user;
    private String application;
}
