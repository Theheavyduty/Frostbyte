package com.example.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "parents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentProfile {

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

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ParentChildRelationship> childRelationships = new HashSet<>();



    public Set<Children> getChildren() {
        Set<Children> children = new HashSet<>();
        for (ParentChildRelationship rel : childRelationships) {
            children.add(rel.getChild());
        }
        return children;
    }
}