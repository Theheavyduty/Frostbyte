package com.example.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "children")
@Getter
@Setter
@NoArgsConstructor
public class Children {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String email;

    @Column
    @JsonIgnore
    private String password;

    @Column
    private Integer phoneNumber;

    @Column
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column
    private LocalDate birthday;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "additional_notes")
    private String additionalNotes;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ParentChildRelationship> parentRelationships = new HashSet<>();

}