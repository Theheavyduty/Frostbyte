package com.example.userservice.controller;

import com.example.userservice.dto.Parent.CreateParentRequest;
import com.example.userservice.dto.Parent.ParentResponse;
import com.example.userservice.model.ParentProfile;
import com.example.userservice.model.User;
import com.example.userservice.service.FileStorageService;
import com.example.userservice.service.ParentProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.userservice.dto.Parent.UpdateParentRequest;


import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parents")
public class ParentController {

    private final ParentProfileService parentProfileService;
    private final FileStorageService fileStorageService;

    public ParentController(ParentProfileService parentProfileService,
                            FileStorageService fileStorageService) {
        this.parentProfileService = parentProfileService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ParentResponse> createParent(@Valid @RequestBody CreateParentRequest req) {
        ParentProfile parent = parentProfileService.createParentWithChildren(
                req.name(),
                req.email(),
                req.password(),
                req.phoneNumber(),
                req.address(),
                req.profilePictureUrl(),   // may be null
                req.childIds()
        );

        ParentResponse response = toResponse(parent);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/{id}/profile-picture", consumes = "multipart/form-data")
    public ResponseEntity<ParentResponse> uploadParentProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        ParentProfile parent = parentProfileService.getById(id);

        String url = fileStorageService.storeParentProfilePicture(id, file);
        parent.setProfilePictureUrl(url);
        parentProfileService.save(parent);

        ParentResponse response = toResponse(parent);
        return ResponseEntity.ok(response);
    }
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ParentResponse> updateParent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateParentRequest req
    ) {
        ParentProfile updated = parentProfileService.updateParent(id, req);
        ParentResponse response = toResponse(updated);
        return ResponseEntity.ok(response);
    }


    private ParentResponse toResponse(ParentProfile parent) {
        return new ParentResponse(
                parent.getId(),
                parent.getName(),
                parent.getEmail(),
                parent.getPhoneNumber(),
                parent.getAddress(),
                parent.getProfilePictureUrl(),
                parent.getChildren().stream()
                        .map(User::getId)
                        .collect(Collectors.toList())
        );
    }
}
