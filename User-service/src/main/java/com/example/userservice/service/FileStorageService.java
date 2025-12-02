package com.example.userservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads/profile-pictures");

    public FileStorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public String storeUserProfilePicture(Long userId, MultipartFile file) throws IOException {
        String extension = getExtension(file.getOriginalFilename());
        String filename = "user-" + userId + "-" + UUID.randomUUID() + extension;

        Path target = rootLocation.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/profile-pictures/" + filename;
    }

    public String storeParentProfilePicture(Long parentId, MultipartFile file) throws IOException {
        String extension = getExtension(file.getOriginalFilename());
        String filename = "parent-" + parentId + "-" + UUID.randomUUID() + extension;

        Path target = rootLocation.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/profile-pictures/" + filename;
    }

    public String storeEmployeeProfilePicture(Long employeeId, MultipartFile file) throws IOException {
        String extension = getExtension(file.getOriginalFilename());
        String filename = "employee-" + employeeId + "-" + UUID.randomUUID() + extension;

        Path target = rootLocation.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/profile-pictures/" + filename;
    }


    private String getExtension(String original) {
        if (original == null) return "";
        int dot = original.lastIndexOf('.');
        return (dot != -1) ? original.substring(dot) : "";
    }
}
