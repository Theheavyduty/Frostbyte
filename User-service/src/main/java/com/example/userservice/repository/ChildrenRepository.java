package com.example.userservice.repository;

import com.example.userservice.model.Children;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChildrenRepository extends JpaRepository<Children,Long> {

    Optional<Children> findByName(String name);

    boolean existsByName(String name);
}
