package com.example.userservice.controller;

import com.example.userservice.dto.Parent.CreateParentRequest;
import com.example.userservice.dto.Parent.ParentResponse;
import com.example.userservice.dto.Parent.UpdateParentRequest;
import com.example.userservice.model.ParentProfile;
import com.example.userservice.service.ParentProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/parents")
public class ParentController {

    private final ParentProfileService parentProfileService;

    public ParentController(ParentProfileService parentProfileService) {
        this.parentProfileService = parentProfileService;
    }

    @GetMapping
    public List<ParentResponse> getAll() {
        return parentProfileService.getAllWithChildren();
    }

    @GetMapping("/{id}")
    public ParentResponse getById(@PathVariable Long id) {
        return parentProfileService.getByIdWithChildren(id);
    }

    @PostMapping
    public ParentProfile create(@RequestBody CreateParentRequest req) {
        return parentProfileService.create(req);
    }

    @PutMapping("/{id}")
    public ParentProfile update(@PathVariable Long id, @RequestBody UpdateParentRequest req) {
        return parentProfileService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        parentProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/profile-picture")
    public ParentProfile uploadProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return parentProfileService.updateProfilePicture(id, file);
    }
}