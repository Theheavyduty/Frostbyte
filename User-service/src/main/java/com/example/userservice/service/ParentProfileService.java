package com.example.userservice.service;

import com.example.userservice.dto.Parent.CreateParentRequest;
import com.example.userservice.dto.Parent.UpdateParentRequest;
import com.example.userservice.model.Children;
import com.example.userservice.model.ParentProfile;
import com.example.userservice.model.Children;
import com.example.userservice.repository.ChildrenRepository;
import com.example.userservice.repository.ParentProfileRepository;
import com.example.userservice.repository.ChildrenRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ParentProfileService {

    public ParentProfileService(ParentProfileRepository parentProfileRepository, ChildrenRepository childrenRepository, FileStorageService fileStorageService) {
        this.parentProfileRepository = parentProfileRepository;
        this.childrenRepository = childrenRepository;
        this.fileStorageService = fileStorageService;
    }

    private final ParentProfileRepository parentProfileRepository;
    private final ChildrenRepository childrenRepository;
    private final FileStorageService fileStorageService;



    // === BASIC CRUD =========================================================

    public List<ParentProfile> getAll() {
        return parentProfileRepository.findAll();
    }

    public ParentProfile getById(Long id) {
        return parentProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found: " + id));
    }

    public ParentProfile create(CreateParentRequest req) {
        ParentProfile parent = new ParentProfile();
        parent.setName(req.name());
        parent.setEmail(req.email());
        parent.setPhoneNumber(req.phoneNumber());
        parent.setAddress(req.address());

        // No password for parents in this app
        parent.setPassword(null);

        parent.setProfilePictureUrl(null);

        // Attach children if IDs provided
        if (req.childIds() != null && !req.childIds().isEmpty()) {
            Set<Children> children = new HashSet<>(childrenRepository.findAllById(req.childIds()));
            parent.setChildren(children);    // now a Set<User>
        }

        return parentProfileRepository.save(parent);
    }


    public ParentProfile update(Long id, UpdateParentRequest req) {
        ParentProfile parent = getById(id);

        if (req.name() != null && !req.name().isBlank()) {
            parent.setName(req.name());
        }
        if (req.email() != null && !req.email().isBlank()) {
            parent.setEmail(req.email());
        }
        if (req.phoneNumber() != null) {
            parent.setPhoneNumber(req.phoneNumber());
        }
        if (req.address() != null && !req.address().isBlank()) {
            parent.setAddress(req.address());
        }

        // Update children if provided (if null, keep existing)
        if (req.childIds() != null && !req.childIds().isEmpty()) {
            Set<Children> children = new HashSet<>(childrenRepository.findAllById(req.childIds()));
            parent.setChildren(children);    // now a Set<User>
        }

        return parentProfileRepository.save(parent);
    }

    public void delete(Long id) {
        ParentProfile parent = getById(id);

        // Delete parent's profile picture
        fileStorageService.deleteProfilePicture(parent.getProfilePictureUrl());

        parentProfileRepository.delete(parent);
    }

    // === PROFILE PICTURE ====================================================

    public ParentProfile updateProfilePicture(Long id, MultipartFile file) throws IOException {
        ParentProfile parent = getById(id);

        // Delete old picture if there is one
        fileStorageService.deleteProfilePicture(parent.getProfilePictureUrl());

        // Store new picture under uploads/profile-pictures/parents/{id}/...
        String url = fileStorageService.storeParentProfilePicture(id, file);
        parent.setProfilePictureUrl(url);

        return parentProfileRepository.save(parent);
    }
}
