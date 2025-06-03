package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.repository.RoleAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleValidatorService {

    @Autowired
    private RoleAssignmentRepository roleAssignmentRepository;

    public boolean isOwner(String bookId, String userId) {
        return hasRole(bookId, userId, "OWNER");
    }

    public boolean isModeratorOrHigher(String bookId, String userId) {
        return hasAnyRole(bookId, userId, List.of("OWNER", "MODERATOR"));
    }

    public boolean hasRole(String bookId, String userId, String role) {
        return roleAssignmentRepository
                .findByBookIdAndUserId(bookId, userId)
                .map(r -> r.getRole().equals(role))
                .orElse(false);
    }

    public boolean hasAnyRole(String bookId, String userId, List<String> roles) {
        return roleAssignmentRepository
                .findByBookIdAndUserId(bookId, userId)
                .map(r -> roles.contains(r.getRole()))
                .orElse(false);
    }
}
