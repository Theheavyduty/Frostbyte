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

    // Sykdom & Fravaer (Optional fields)
    private LocalDateTime eventTime; // Sykdom - logg tidspunkt  ex) 2025-12-10T15:35:00 (DB - Postgres)
    private String symptoms; // filled in only for Sykdom
    private String absenceReason; // filled in only for FRAVAER

    private Long registeredByEmployeeId;
    //TODO : both symptoms and absenceReason is nullable in DB
}
