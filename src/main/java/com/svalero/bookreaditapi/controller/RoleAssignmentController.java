package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.BookPage;
import com.svalero.bookreaditapi.domain.RoleAssignment;
import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.repository.BookPageRepository;
import com.svalero.bookreaditapi.repository.RoleAssignmentRepository;
import com.svalero.bookreaditapi.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
public class RoleAssignmentController {

    @Autowired
    private RoleAssignmentRepository roleAssignmentRepository;

    @Autowired
    private BookPageRepository bookPageRepository;

    @Autowired
    private SecurityUtils securityUtils;

    private static final List<String> VALID_ROLES = List.of("OWNER", "MODERATOR");

    @PostMapping
    public ResponseEntity<?> assignRole(@RequestBody RoleAssignment request,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = securityUtils.getCurrentUser(userDetails);

        BookPage book = bookPageRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));

        // Validación de permisos
        Optional<RoleAssignment> currentUserRole = roleAssignmentRepository
                .findByBookIdAndUserId(request.getBookId(), currentUser.getUserId());

        if (currentUserRole.isEmpty() || !currentUserRole.get().getRole().equals("OWNER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado");
        }

        // Validación de rol válido
        if (!VALID_ROLES.contains(request.getRole())) {
            return ResponseEntity.badRequest().body("Rol no válido");
        }

        // Si ya existe, actualizamos
        Optional<RoleAssignment> existing = roleAssignmentRepository
                .findByBookIdAndUserId(request.getBookId(), request.getUserId());

        if (existing.isPresent()) {
            RoleAssignment role = existing.get();
            role.setRole(request.getRole());
            roleAssignmentRepository.save(role);
            return ResponseEntity.ok(role);
        }

        // Si no existe, creamos nuevo
        request.setAssignmentId(UUID.randomUUID().toString());
        roleAssignmentRepository.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @GetMapping("/book/{bookId}")
    public List<RoleAssignment> getRolesForBook(@PathVariable String bookId) {
        return roleAssignmentRepository.findByBookId(bookId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public List<RoleAssignment> getRolesForUser(@PathVariable String userId) {
        return roleAssignmentRepository.findByUserId(userId);
    }

    @GetMapping("/me")
    public List<RoleAssignment> getRolesForCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = securityUtils.getCurrentUser(userDetails);
        return roleAssignmentRepository.findByUserId(currentUser.getUserId());
    }

    @DeleteMapping
    public ResponseEntity<?> removeRole(@RequestParam String bookId,
                                        @RequestParam String userId,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = securityUtils.getCurrentUser(userDetails);

        Optional<RoleAssignment> currentUserRole = roleAssignmentRepository
                .findByBookIdAndUserId(bookId, currentUser.getUserId());

        if (currentUserRole.isEmpty() || !currentUserRole.get().getRole().equals("OWNER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado");
        }

        Optional<RoleAssignment> existing = roleAssignmentRepository
                .findByBookIdAndUserId(bookId, userId);

        if (existing.isPresent()) {
            roleAssignmentRepository.deleteById(existing.get().getAssignmentId());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
