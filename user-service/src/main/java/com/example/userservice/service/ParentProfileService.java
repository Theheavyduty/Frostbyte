package com.example.userservice.service;

import com.example.userservice.dto.Parent.CreateParentRequest;
import com.example.userservice.dto.Parent.UpdateParentRequest;
import com.example.userservice.dto.Parent.ParentResponse;
import com.example.userservice.model.Children;
import com.example.userservice.model.ParentChildRelationship;
import com.example.userservice.model.ParentProfile;
import com.example.userservice.repository.ChildrenRepository;
import com.example.userservice.repository.ParentChildRelationshipRepository;
import com.example.userservice.repository.ParentProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ParentProfileService {

    private final ParentProfileRepository parentProfileRepository;
    private final ChildrenRepository childrenRepository;
    private final ParentChildRelationshipRepository relationshipRepository;
    private final FileStorageService fileStorageService;

    public ParentProfileService(ParentProfileRepository parentProfileRepository,
                                ChildrenRepository childrenRepository,
                                ParentChildRelationshipRepository relationshipRepository,
                                FileStorageService fileStorageService) {
        this.parentProfileRepository = parentProfileRepository;
        this.childrenRepository = childrenRepository;
        this.relationshipRepository = relationshipRepository;
        this.fileStorageService = fileStorageService;
    }


    private ParentResponse toResponse(ParentProfile parent) {
        List<ParentChildRelationship> relationships = relationshipRepository.findByParentId(parent.getId());

        List<ParentResponse.ChildRelationshipSummary> childSummaries = relationships.stream()
                .map(rel -> {
                    ParentResponse.DepartmentSummary deptSummary = null;
                    if (rel.getChild().getDepartment() != null) {
                        deptSummary = new ParentResponse.DepartmentSummary(
                                rel.getChild().getDepartment().getId(),
                                rel.getChild().getDepartment().getName()
                        );
                    }
                    return new ParentResponse.ChildRelationshipSummary(
                            rel.getRegistrationNumber(),
                            rel.getChild().getId(),
                            rel.getChild().getName(),
                            rel.getChild().getEmail(),
                            rel.getChild().getPhoneNumber(),
                            deptSummary,
                            rel.getChild().getBirthday()
                    );
                })
                .toList();

        return new ParentResponse(
                parent.getId(),
                parent.getName(),
                parent.getEmail(),
                parent.getPhoneNumber(),
                parent.getAddress(),
                parent.getProfilePictureUrl(),
                childSummaries
        );
    }


    public List<ParentResponse> getAllWithChildren() {
        return parentProfileRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ParentResponse getByIdWithChildren(Long id) {
        ParentProfile parent = parentProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found: " + id));
        return toResponse(parent);
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
        parent.setPassword(null);
        parent.setProfilePictureUrl(null);

        ParentProfile savedParent = parentProfileRepository.save(parent);

        if (req.childIds() != null && !req.childIds().isEmpty()) {
            List<Children> children = childrenRepository.findAllById(req.childIds());
            for (Children child : children) {
                Integer registrationNumber = relationshipRepository.getNextRegistrationNumberForChild(child.getId());
                ParentChildRelationship relationship = ParentChildRelationship.builder()
                        .parent(savedParent)
                        .child(child)
                        .registrationNumber(registrationNumber)
                        .build();
                relationshipRepository.save(relationship);
            }
        }

        return savedParent;
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

        if (req.childIds() != null) {
            List<ParentChildRelationship> existingRelationships = relationshipRepository.findByParentId(id);
            relationshipRepository.deleteAll(existingRelationships);

            if (!req.childIds().isEmpty()) {
                List<Children> children = childrenRepository.findAllById(req.childIds());
                for (Children child : children) {
                    Integer registrationNumber = relationshipRepository.getNextRegistrationNumberForChild(child.getId());
                    ParentChildRelationship relationship = ParentChildRelationship.builder()
                            .parent(parent)
                            .child(child)
                            .registrationNumber(registrationNumber)
                            .build();
                    relationshipRepository.save(relationship);
                }
            }
        }

        return parentProfileRepository.save(parent);
    }

    public void delete(Long id) {
        ParentProfile parent = getById(id);

        fileStorageService.deleteProfilePicture(parent.getProfilePictureUrl());

        List<ParentChildRelationship> relationships = relationshipRepository.findByParentId(id);
        relationshipRepository.deleteAll(relationships);

        parentProfileRepository.delete(parent);
    }


    public ParentProfile updateProfilePicture(Long id, MultipartFile file) throws IOException {
        ParentProfile parent = getById(id);

        fileStorageService.deleteProfilePicture(parent.getProfilePictureUrl());

        String url = fileStorageService.storeParentProfilePicture(id, file);
        parent.setProfilePictureUrl(url);

        return parentProfileRepository.save(parent);
    }
}