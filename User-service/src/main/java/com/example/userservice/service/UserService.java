package com.example.userservice.service;

import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.dto.UpdateUserRequest;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    public User register(RegisterRequest req) {
        if (existsByName(req.name())) {
            throw new IllegalStateException("Name already taken");
        }

        User user = new User();
        user.setName(req.name());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setPhoneNumber(req.phoneNumber());
        user.setAddress(req.address());
        user.setProfilePictureUrl(req.profilePictureUrl());

        return userRepository.save(user);
    }

    public User updateUser(Long id, UpdateUserRequest req) {
        User user = getById(id);

        if (!user.getName().equals(req.name())) {
            userRepository.findByName(req.name())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) {
                            throw new IllegalStateException("Name already taken");
                        }
                    });
        }

        user.setName(req.name());
        user.setEmail(req.email());
        user.setPhoneNumber(req.phoneNumber());
        user.setAddress(req.address());
        user.setProfilePictureUrl(req.profilePictureUrl());

        return userRepository.save(user);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
