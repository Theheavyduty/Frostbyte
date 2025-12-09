package com.example.frostbyte.checkservice.childstatus.repository;

import com.example.frostbyte.checkservice.childstatus.domain.ChildStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//JpaRepository offers basic CRUD methods, paging & array
@Repository
public interface ChildStatusLogRepo extends JpaRepository<ChildStatusLog,Long> {

}
