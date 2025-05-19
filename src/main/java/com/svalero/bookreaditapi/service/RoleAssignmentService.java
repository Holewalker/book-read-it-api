package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.RoleAssignment;
import com.svalero.bookreaditapi.repository.RoleAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleAssignmentService {
    @Autowired
    private RoleAssignmentRepository repository;

    public void saveRole(RoleAssignment role) {
        repository.save(role);
    }
}
