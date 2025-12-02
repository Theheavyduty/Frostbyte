package com.example.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Employee login / display name
    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private Integer phoneNumber;

    @Column
    private String address;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;
}
