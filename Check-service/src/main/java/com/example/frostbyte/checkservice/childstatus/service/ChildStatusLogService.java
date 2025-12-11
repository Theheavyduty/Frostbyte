package com.example.frostbyte.checkservice.childstatus.service;

import com.example.frostbyte.checkservice.childstatus.api.requests.RegisterChildStatusRequest;
import com.example.frostbyte.checkservice.childstatus.api.requests.RegisterFravaerRequest;
import com.example.frostbyte.checkservice.childstatus.api.requests.RegisterSykRequest;
import com.example.frostbyte.checkservice.childstatus.api.response.ChildStatusResponse;
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
                request.symptoms(),
                null,
                request.employeeId()

        );
        return Repo.save(log);

    }

    public ChildStatusLog registerFravaer(RegisterFravaerRequest request) {
        var log = new ChildStatusLog(
                null,
                request.childId(),
                ChildStatus.FRAVAER,
                LocalDateTime.now(),
                null,
                request.absenceReasons(),
                request.employeeId()
        );
        return Repo.save(log);
    }

    public ChildStatusLog registerChildStatus(RegisterChildStatusRequest request) {
        var log = new ChildStatusLog(
                null,
                request.childId(),
                request.status(),
                LocalDateTime.now(),
                null,
                null,
                request.employeeId()
        );
        return Repo.save(log);
    }

    // If status not exist return null -> Frontend show "ingen status registrert ennå"
    public ChildStatusLog getChildStatusLatest(Long childId) {
        return Repo.findFirstByChildIdOrderByEventTimeDesc(childId);
    }

    public ChildStatusResponse toResponse(ChildStatusLog log) {
        if (log == null) return null;
        return new ChildStatusResponse(
                log.getChildId(),
                log.getStatus(),
                log.getEventTime(),
                log.getSymptoms(),
                log.getAbsenceReason()
        );
    }
}
