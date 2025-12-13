package com.example.frostbyte.checkservice.childstatus.api;

import com.example.frostbyte.checkservice.childstatus.api.requests.RegisterChildStatusRequest;
import com.example.frostbyte.checkservice.childstatus.api.requests.RegisterFravaerRequest;
import com.example.frostbyte.checkservice.childstatus.api.requests.RegisterSykRequest;
import com.example.frostbyte.checkservice.childstatus.api.requests.UpdateChildStatusRequest;
import com.example.frostbyte.checkservice.childstatus.api.response.ChildStatusResponse;
import com.example.frostbyte.checkservice.childstatus.domain.ChildStatusLog;
import com.example.frostbyte.checkservice.childstatus.service.ChildStatusLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/child-status")
public class ChildStatusLogController {
    private final ChildStatusLogService service;

    public ChildStatusLogController(ChildStatusLogService service) {
        this.service = service;
    }

    @PostMapping("/checks")
    public ResponseEntity<ChildStatusLog> registerChildStatus(
            @Valid @RequestBody RegisterChildStatusRequest request) {

        var saved = service.registerChildStatus(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/logs/{id}")
    public ResponseEntity<ChildStatusLog> updateEventTime(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChildStatusRequest request) {
        var updated = service.updateEventTime(id, request.eventTime());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/logs/{id}")
    public ResponseEntity<ChildStatusLog> getById(@PathVariable Long id) {
        var log = service.getById(id);
        if (log == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(log);
    }

    @GetMapping("/children/{childId}/latest")
    public ResponseEntity<ChildStatusResponse> getChildStatusLatestByChildId(
            @PathVariable Long childId) {
        var log =  service.getChildStatusLatest(childId);
        if (log == null) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(service.toResponse(log)); // 200
    }

    @PostMapping("/syk")
    public ResponseEntity<ChildStatusLog> registerSyk(
            @Valid @RequestBody RegisterSykRequest request) {
        var saved =  service.registerSyk(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/fravaer")
    public ResponseEntity<ChildStatusLog> registerFravaer(
            @Valid @RequestBody RegisterFravaerRequest request) {
        var saved =  service.registerFravaer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}