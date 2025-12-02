package com.example.userservice.controller;

import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.model.User;
import com.example.userservice.service.FileStorageService;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.userservice.dto.UpdateUserRequest;


import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserServiceController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    public UserServiceController(UserService userService,
                                 FileStorageService fileStorageService) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest req) {
        if (userService.existsByName(req.name())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User u = userService.register(req);
        UserResponse body = new UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getPhoneNumber(),
                u.getAddress(),
                u.getProfilePictureUrl()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping(value = "/{id}/profile-picture", consumes = "multipart/form-data")
    public ResponseEntity<UserResponse> uploadProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        User user = userService.getById(id);

        String url = fileStorageService.storeUserProfilePicture(id, file);
        user.setProfilePictureUrl(url);
        userService.save(user);

        UserResponse resp = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getProfilePictureUrl()
        );

        return ResponseEntity.ok(resp);
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest req
    ) {
        User updated = userService.updateUser(id, req);

        UserResponse resp = new UserResponse(
                updated.getId(),
                updated.getName(),
                updated.getEmail(),
                updated.getPhoneNumber(),
                updated.getAddress(),
                updated.getProfilePictureUrl()
        );

        return ResponseEntity.ok(resp);
    }


    @GetMapping("/ping")
    public String ping() {
        return "User service is alive";
    }
}
