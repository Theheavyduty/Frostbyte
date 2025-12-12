package com.example.userservice.repository;

import com.example.userservice.model.ParentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParentProfileRepository extends JpaRepository<ParentProfile, Long> {

    Optional<ParentProfile> findByName(String name);

    boolean existsByName(String name);

    List<ParentProfile> findByChildren_Id(Long childId);
}
