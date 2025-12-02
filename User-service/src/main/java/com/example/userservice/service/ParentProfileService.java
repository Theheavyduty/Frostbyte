package com.example.userservice.service;

import com.example.userservice.model.ParentProfile;
import com.example.userservice.model.User;
import com.example.userservice.repository.ParentProfileRepository;
import com.example.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.userservice.dto.Parent.UpdateParentRequest;


import java.util.List;

@Service
public class ParentProfileService {

    private final ParentProfileRepository parentRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public ParentProfileService(ParentProfileRepository parentRepo,
                                UserRepository userRepo,
                                PasswordEncoder passwordEncoder) {
        this.parentRepo = parentRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public ParentProfile createParentWithChildren(
            String name,
            String email,
            String rawPassword,
            Integer phoneNumber,
            String address,
            String profilePictureUrl,   // NEW
            List<Long> childIds
    ) {
        if (parentRepo.existsByName(name)) {
            throw new IllegalStateException("Parent name already taken");
        }

        ParentProfile parent = new ParentProfile();
        parent.setName(name);
        parent.setEmail(email);
        parent.setPassword(passwordEncoder.encode(rawPassword));
        parent.setPhoneNumber(phoneNumber);
        parent.setAddress(address);
        parent.setProfilePictureUrl(profilePictureUrl); // NEW

        List<User> children = userRepo.findAllById(childIds);
        parent.getChildren().addAll(children);

        return parentRepo.save(parent);
    }

    public ParentProfile updateParent(Long id, UpdateParentRequest req) {
        ParentProfile parent = getById(id);

        if (!parent.getName().equals(req.name())) {
            parentRepo.findByName(req.name())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) {
                            throw new IllegalStateException("Parent name already taken");
                        }
                    });
        }

        parent.setName(req.name());
        parent.setEmail(req.email());
        parent.setPhoneNumber(req.phoneNumber());
        parent.setAddress(req.address());
        parent.setProfilePictureUrl(req.profilePictureUrl());

        // Replace children with the new list
        parent.getChildren().clear();
        if (req.childIds() != null && !req.childIds().isEmpty()) {
            List<User> children = userRepo.findAllById(req.childIds());
            parent.getChildren().addAll(children);
        }

        return parentRepo.save(parent);
    }


    public ParentProfile getById(Long id) {
        return parentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parent not found: " + id));
    }

    public ParentProfile save(ParentProfile parent) {
        return parentRepo.save(parent);
    }
}
