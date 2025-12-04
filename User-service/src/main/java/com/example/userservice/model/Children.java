package com.example.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "children") // children
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

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column
    private Integer phoneNumber;

    @Column
    private String address;

    @Column
    private String departments;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

}
