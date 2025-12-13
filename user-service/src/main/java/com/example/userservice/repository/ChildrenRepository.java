package com.example.userservice.repository;

import com.example.userservice.model.Children;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildrenRepository extends JpaRepository<Children, Long> {


    List<Children> findByDepartmentId(Long departmentId);
}