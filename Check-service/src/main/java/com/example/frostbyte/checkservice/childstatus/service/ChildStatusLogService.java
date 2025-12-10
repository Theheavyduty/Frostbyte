package com.example.frostbyte.checkservice.childstatus.service;

import com.example.frostbyte.checkservice.childstatus.api.requests.RegisterFravaerRequest;
import com.example.frostbyte.checkservice.childstatus.api.requests.RegisterSykRequest;
import com.example.frostbyte.checkservice.childstatus.domain.ChildStatus;
import com.example.frostbyte.checkservice.childstatus.domain.ChildStatusLog;
import com.example.frostbyte.checkservice.childstatus.repository.ChildStatusLogRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChildStatusLogService {

    private final ChildStatusLogRepo Repo;

    public ChildStatusLogService(ChildStatusLogRepo Repo) {
        this.Repo = Repo;
    }

    public ChildStatusLog registerSyk(RegisterSykRequest request) {
        var log = new ChildStatusLog(
                null,
                request.childId(),
                ChildStatus.SYK,
                request.sicknessTime(),
                request.employeeId(),
                request.symptoms(),
                null
        );
        return Repo.save(log);

    }

    public ChildStatusLog registerFravaer(RegisterFravaerRequest request) {
        var log = new ChildStatusLog(
                null,
                request.childId(),
                ChildStatus.FRAVAER,
                LocalDateTime.now(),
                request.employeeId(),
                null,
                request.absenceReasons()
        );
        return Repo.save(log);
    }
}
