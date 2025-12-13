package com.example.userservice.controller;

import com.example.userservice.dto.Children.ChildrenResponse;
import com.example.userservice.dto.Children.CreateChildrenRequest;
import com.example.userservice.dto.Children.UpdateChildrenRequest;
import com.example.userservice.service.ChildrenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/children")
public class ChildrenController {

    private final ChildrenService childrenService;

    public ChildrenController(ChildrenService childrenService) {
        this.childrenService = childrenService;
    }

    @GetMapping
    public List<ChildrenResponse> getAll() {
        return childrenService.getAllWithParents();
    }

    @GetMapping("/{id}")
    public ChildrenResponse getById(@PathVariable Long id) {
        return childrenService.getByIdWithParents(id);
    }

    @PostMapping
    public ChildrenResponse create(@RequestBody CreateChildrenRequest req) {
        return childrenService.create(req);
    }

    @PutMapping("/{id}")
    public ChildrenResponse update(@PathVariable Long id, @RequestBody UpdateChildrenRequest req) {
        return childrenService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        childrenService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/profile-picture")
    public ChildrenResponse uploadProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return childrenService.updateProfilePicture(id, file);
    }
}