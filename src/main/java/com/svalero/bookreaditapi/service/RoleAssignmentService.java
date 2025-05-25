package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.RoleAssignment;
import com.svalero.bookreaditapi.repository.RoleAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleAssignmentService {
    @Autowired
    private RoleAssignmentRepository roleAssignmentRepository;

    public void saveRole(RoleAssignment role) {
        roleAssignmentRepository.save(role);
    }

    public Optional<RoleAssignment> findByBookIdAndUserId(String bookId, String userId) {
        return roleAssignmentRepository.findByBookIdAndUserId(bookId, userId);
    }

    public List<RoleAssignment> findById(String id) {
        return roleAssignmentRepository.findByUserId(id);
    }

    public List<RoleAssignment> findAllByBookId(String bookId) {
        return roleAssignmentRepository.findAllByBookId(bookId);
    }

    public void deleteById(String id) {
        roleAssignmentRepository.deleteByUserId(id);
    }
}
