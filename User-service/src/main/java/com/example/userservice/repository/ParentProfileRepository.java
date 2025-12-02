package com.example.userservice.repository;

import com.example.userservice.model.ParentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentProfileRepository extends JpaRepository<ParentProfile, Long> {
    Optional<ParentProfile> findByName(String name);
    boolean existsByName(String name);
}
