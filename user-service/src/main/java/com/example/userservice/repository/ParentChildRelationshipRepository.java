package com.example.userservice.repository;

import com.example.userservice.model.ParentChildRelationship;
import com.example.userservice.model.ParentChildRelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentChildRelationshipRepository extends JpaRepository<ParentChildRelationship, ParentChildRelationshipId> {

    List<ParentChildRelationship> findByChildId(Long childId);

    List<ParentChildRelationship> findByParentId(Long parentId);


    @Query("SELECT r FROM ParentChildRelationship r JOIN FETCH r.parent WHERE r.child.id = :childId")
    List<ParentChildRelationship> findByChildIdWithParent(@Param("childId") Long childId);

    @Query("SELECT COALESCE(MAX(r.registrationNumber), 0) + 1 FROM ParentChildRelationship r WHERE r.child.id = :childId")
    Integer getNextRegistrationNumberForChild(@Param("childId") Long childId);
}