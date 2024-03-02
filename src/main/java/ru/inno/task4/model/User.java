package ru.inno.task4.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "stepup_users")
@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userName;
    private String fio;
}
