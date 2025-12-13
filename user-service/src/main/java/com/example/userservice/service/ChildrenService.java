package com.example.userservice.service;

import com.example.userservice.dto.Children.CreateChildrenRequest;
import com.example.userservice.dto.Children.UpdateChildrenRequest;
import com.example.userservice.dto.Children.ChildrenResponse;
import com.example.userservice.model.Children;
import com.example.userservice.model.Department;
import com.example.userservice.model.ParentChildRelationship;
import com.example.userservice.model.ParentProfile;
import com.example.userservice.repository.ChildrenRepository;
import com.example.userservice.repository.DepartmentRepository;
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
public class ChildrenService {

    private final ChildrenRepository childrenRepository;
    private final ParentProfileRepository parentProfileRepository;
    private final ParentChildRelationshipRepository relationshipRepository;
    private final DepartmentRepository departmentRepository;
    private final FileStorageService fileStorageService;

    public ChildrenService(ChildrenRepository childrenRepository,
                           ParentProfileRepository parentProfileRepository,
                           ParentChildRelationshipRepository relationshipRepository,
                           DepartmentRepository departmentRepository,
                           FileStorageService fileStorageService) {
        this.childrenRepository = childrenRepository;
        this.parentProfileRepository = parentProfileRepository;
        this.relationshipRepository = relationshipRepository;
        this.departmentRepository = departmentRepository;
        this.fileStorageService = fileStorageService;
    }


    private ChildrenResponse toResponse(Children child) {
        List<ParentChildRelationship> relationships = relationshipRepository.findByChildIdWithParent(child.getId());

        List<ChildrenResponse.ParentRelationshipSummary> parentSummaries = relationships.stream()
                .map(rel -> new ChildrenResponse.ParentRelationshipSummary(
                        rel.getRegistrationNumber(),
                        rel.getParent().getId(),
                        rel.getParent().getName(),
                        rel.getParent().getEmail(),
                        rel.getParent().getPhoneNumber()
                ))
                .toList();

        ChildrenResponse.DepartmentSummary departmentSummary = null;
        if (child.getDepartment() != null) {
            departmentSummary = new ChildrenResponse.DepartmentSummary(
                    child.getDepartment().getId(),
                    child.getDepartment().getName()
            );
        }

        return new ChildrenResponse(
                child.getId(),
                child.getName(),
                child.getEmail(),
                child.getPhoneNumber(),
                child.getAddress(),
                departmentSummary,
                child.getBirthday(),
                child.getProfilePictureUrl(),
                child.getAdditionalNotes(),
                parentSummaries
        );
    }


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

    public ChildrenResponse create(CreateChildrenRequest req) {
        Department department = departmentRepository.findById(req.departmentId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Department not found: " + req.departmentId() + ". Departments can only be created by employees."));

        Children children = new Children();
        children.setName(req.name());
        children.setEmail(req.email());
        children.setPhoneNumber(req.phoneNumber());
        children.setAddress(req.address());
        children.setDepartment(department);
        children.setBirthday(req.birthday());
        children.setAdditionalNotes(req.additionalNotes());

        children.setPassword(null);
        children.setProfilePictureUrl(null);

        Children saved = childrenRepository.save(children);
        return toResponse(saved);
    }

    public ChildrenResponse update(Long id, UpdateChildrenRequest req) {
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
        if (req.departmentId() != null) {
            Department department = departmentRepository.findById(req.departmentId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Department not found: " + req.departmentId() + ". Departments can only be created by employees."));
            children.setDepartment(department);
        }
        if (req.birthday() != null) {
            children.setBirthday(req.birthday());
        }
        if (req.additionalNotes() != null) {
            children.setAdditionalNotes(req.additionalNotes());
        }

        Children saved = childrenRepository.save(children);
        return toResponse(saved);
    }

    public void delete(Long id) {
        Children children = getById(id);

        fileStorageService.deleteProfilePicture(children.getProfilePictureUrl());

        List<ParentChildRelationship> relationships = relationshipRepository.findByChildId(id);

        for (ParentChildRelationship rel : relationships) {
            ParentProfile parent = rel.getParent();

            relationshipRepository.delete(rel);

            List<ParentChildRelationship> remainingRelationships = relationshipRepository.findByParentId(parent.getId());
            if (remainingRelationships.isEmpty() ||
                    (remainingRelationships.size() == 1 && remainingRelationships.get(0).getChild().getId().equals(id))) {
                fileStorageService.deleteProfilePicture(parent.getProfilePictureUrl());
                parentProfileRepository.delete(parent);
            }
        }

        childrenRepository.delete(children);
    }


    public ChildrenResponse updateProfilePicture(Long id, MultipartFile file) throws IOException {
        Children children = getById(id);

        fileStorageService.deleteProfilePicture(children.getProfilePictureUrl());

        String url = fileStorageService.storeUserProfilePicture(id, file);
        children.setProfilePictureUrl(url);

        Children saved = childrenRepository.save(children);
        return toResponse(saved);
    }


    public List<ChildrenResponse> getChildrenByDepartmentId(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new EntityNotFoundException("Department not found: " + departmentId);
        }

        return childrenRepository.findByDepartmentId(departmentId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ChildrenResponse> getChildrenByDepartmentName(String departmentName) {
        Department department = departmentRepository.findByName(departmentName)
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + departmentName));

        return childrenRepository.findByDepartmentId(department.getId()).stream()
                .map(this::toResponse)
                .toList();
    }
}