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
            Files.createDirectories(rootLocation.resolve("children"));
            Files.createDirectories(rootLocation.resolve("parents"));
            Files.createDirectories(rootLocation.resolve("employees"));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directories", e);
        }
    }

    public String storeUserProfilePicture(Long userId, MultipartFile file) throws IOException {
        return storeProfilePicture("children", "children-" + userId, file);
    }

    public String storeParentProfilePicture(Long parentId, MultipartFile file) throws IOException {
        return storeProfilePicture("parents", "parent-" + parentId, file);
    }

    public String storeEmployeeProfilePicture(Long employeeId, MultipartFile file) throws IOException {
        return storeProfilePicture("employees", "employee-" + employeeId, file);
    }

    public void deleteProfilePicture(String url) {
        if (url == null || !url.startsWith("/profile-pictures/")) {
            return;
        }

        String relative = url.substring("/profile-pictures/".length());
        Path target = rootLocation.resolve(relative).normalize();

        try {
            Files.deleteIfExists(target);
        } catch (IOException e) {
        }
    }

    private String storeProfilePicture(String subFolder, String prefix, MultipartFile file) throws IOException {
        String extension = getExtension(file.getOriginalFilename());
        String filename = prefix + "-" + UUID.randomUUID() + extension;

        Path subDir = rootLocation.resolve(subFolder);
        Files.createDirectories(subDir);

        Path target = subDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/profile-pictures/" + subFolder + "/" + filename;
    }

    private String getExtension(String original) {
        if (original == null) return "";
        int dot = original.lastIndexOf('.');
        return (dot != -1) ? original.substring(dot) : "";
    }
}
