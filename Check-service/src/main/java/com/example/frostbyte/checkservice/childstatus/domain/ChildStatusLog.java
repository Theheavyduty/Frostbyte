package com.example.frostbyte.checkservice.childstatus.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "child_status_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChildStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private Long childId;

    @Enumerated(EnumType.STRING)
    private ChildStatus status; // HENTET / LEVERT / FRAVAER / SYK

    @Enumerated(EnumType.STRING)
    private KindergartenDepartment department;
    private LocalDateTime eventTime; // Sykdom - logg tidspunkt
    private Long registeredByEmployeeId;

    private String symptoms; // filled in only for Sykdom
    private String absenceReason; // filled in only for FRAVAER

    //TODO: Need  `status == SYK` -> symptoms
    //TODO: Need `status == FRAVAER` -> absenceReason
    //TODO : both symptoms and absenceReason is nullable in DB
}
