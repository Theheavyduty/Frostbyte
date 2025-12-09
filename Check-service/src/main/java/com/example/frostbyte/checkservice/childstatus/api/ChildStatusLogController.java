package com.example.frostbyte.checkservice.childstatus.api;

import com.example.frostbyte.checkservice.childstatus.api.requests.RegisterFravaerRequest;
import com.example.frostbyte.checkservice.childstatus.api.requests.RegisterSykRequest;
import com.example.frostbyte.checkservice.childstatus.domain.ChildStatusLog;
import com.example.frostbyte.checkservice.childstatus.service.ChildStatusLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/child-status")
public class ChildStatusLogController {
    private final ChildStatusLogService service;

    public ChildStatusLogController(ChildStatusLogService service) {
        this.service = service;
    }

    @PostMapping("/syk")
    @ResponseStatus(HttpStatus.CREATED)
    public ChildStatusLog registerSyk(@Valid @RequestBody RegisterSykRequest request) {
        return service.registerSyk(request);
    }

    @PostMapping("/fravaer")
    @ResponseStatus(HttpStatus.CREATED)
    public ChildStatusLog registerFravaer(@Valid @RequestBody RegisterFravaerRequest request) {
        return service.registerFravaer(request);
    }
}
