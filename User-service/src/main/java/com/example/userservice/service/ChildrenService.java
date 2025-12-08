package com.example.userservice.service;

import com.example.userservice.dto.Children.CreateChildrenRequest;
import com.example.userservice.dto.Children.UpdateChildrenRequest;
import com.example.userservice.dto.Children.ChildrenResponse;
import com.example.userservice.model.Children;
import com.example.userservice.model.ParentProfile;
import com.example.userservice.repository.ChildrenRepository;
import com.example.userservice.repository.ParentProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ChildrenService {

    private final ChildrenRepository childrenRepository;
    private final ParentProfileRepository parentProfileRepository;
    private final FileStorageService fileStorageService;

    public ChildrenService(ChildrenRepository childrenRepository,
                           ParentProfileRepository parentProfileRepository,
                           FileStorageService fileStorageService) {
        this.childrenRepository = childrenRepository;
        this.parentProfileRepository = parentProfileRepository;
        this.fileStorageService = fileStorageService;
    }

    // === RESPONSE CONVERSION ================================================

    private ChildrenResponse toResponse(Children child) {
        List<ParentProfile> parents = parentProfileRepository.findByChildren_Id(child.getId());

        List<ChildrenResponse.ParentSummary> parentSummaries = parents.stream()
                .map(p -> new ChildrenResponse.ParentSummary(
                        p.getId(),
                        p.getName(),
                        p.getEmail(),
                        p.getPhoneNumber()
                ))
                .toList();

        return new ChildrenResponse(
                child.getId(),
                child.getName(),
                child.getEmail(),
                child.getPhoneNumber(),
                child.getAddress(),
                child.getDepartments(),
                child.getBirthday(),
                child.getProfilePictureUrl(),
                parentSummaries
        );
    }

    // === BASIC CRUD =========================================================

    public List<ChildrenResponse> getAllWithParents() {
        return childrenRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ChildrenResponse getByIdWithParents(Long id) {
        Children child = childrenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Child not found: " + id));
        return toResponse(child);
    }

    public Children getById(Long id) {
        return childrenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Child not found: " + id));
    }

    public Children create(CreateChildrenRequest req) {
        Children children = new Children();
        children.setName(req.name());
        children.setEmail(req.email());
        children.setPhoneNumber(req.phoneNumber());
        children.setAddress(req.address());
        children.setDepartments(req.departments());
        children.setBirthday(req.birthday());

        children.setPassword(null);
        children.setProfilePictureUrl(null);

        return childrenRepository.save(children);
    }

    public Children update(Long id, UpdateChildrenRequest req) {
        Children children = getById(id);

        if (req.name() != null && !req.name().isBlank()) {
            children.setName(req.name());
        }
        if (req.email() != null && !req.email().isBlank()) {
            children.setEmail(req.email());
        }
        if (req.phoneNumber() != null) {
            children.setPhoneNumber(req.phoneNumber());
        }
        if (req.address() != null && !req.address().isBlank()) {
            children.setAddress(req.address());
        }
        if (req.departments() != null && !req.departments().isBlank()) {
            children.setDepartments(req.departments());
        }
        if (req.birthday() != null) {
            children.setBirthday(req.birthday());
        }
        // profilePictureUrl handled via updateProfilePicture
        return childrenRepository.save(children);
    }

    public void delete(Long id) {
        Children children = getById(id);

        // Remove child's image from disk
        fileStorageService.deleteProfilePicture(children.getProfilePictureUrl());

        // Remove child from all parents; delete parents that end up without children
        List<ParentProfile> parents = parentProfileRepository.findByChildren_Id(id);
        for (ParentProfile parent : parents) {
            parent.getChildren().removeIf(c -> c.getId().equals(id));

            if (parent.getChildren().isEmpty()) {
                fileStorageService.deleteProfilePicture(parent.getProfilePictureUrl());
                parentProfileRepository.delete(parent);
            } else {
                parentProfileRepository.save(parent);
            }
        }

        childrenRepository.delete(children);
    }

    // === PROFILE PICTURE ====================================================

    public Children updateProfilePicture(Long id, MultipartFile file) throws IOException {
        Children children = getById(id);

        // Delete old picture if it exists
        fileStorageService.deleteProfilePicture(children.getProfilePictureUrl());

        // Store new picture under uploads/profile-pictures/children/{id}/...
        String url = fileStorageService.storeUserProfilePicture(id, file);
        children.setProfilePictureUrl(url);

        return childrenRepository.save(children);
    }
}