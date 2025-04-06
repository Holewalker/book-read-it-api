package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.RoleAssignment;
import com.svalero.bookreaditapi.domain.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface RoleAssignmentRepository extends CrudRepository<RoleAssignment, String> {
    List<RoleAssignment> findByBookId(String bookId);
    List<RoleAssignment> findByUserId(String userId);
    Optional<RoleAssignment> findByBookIdAndUserId(String bookId, String userId);
}

