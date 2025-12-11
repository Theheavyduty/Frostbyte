package com.example.frostbyte.checkservice.childstatus.repository;

import com.example.frostbyte.checkservice.childstatus.domain.ChildStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChildStatusLogRepo extends JpaRepository<ChildStatusLog,Long> {
    ChildStatusLog findFirstByChildIdOrderByEventTimeDesc(Long childId);
}
