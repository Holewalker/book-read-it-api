package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.BookPage;
import com.svalero.bookreaditapi.domain.DTO.UserDTO;
import com.svalero.bookreaditapi.domain.RoleAssignment;
import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.service.BookPageService;
import com.svalero.bookreaditapi.service.RoleAssignmentService;
import com.svalero.bookreaditapi.service.RoleValidatorService;
import com.svalero.bookreaditapi.service.UserService;
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
    //private RoleAssignmentRepository roleAssignmentRepository;
    private RoleAssignmentService roleAssignmentService;

    @Autowired
    //private BookPageRepository bookPageRepository;
    private BookPageService bookPageService;
    @Autowired
    private SecurityUtils securityUtils;

    private static final List<String> VALID_ROLES = List.of("OWNER", "MODERATOR");
    @Autowired
    private UserService userService;


    @PostMapping("/{bookId}")
    public ResponseEntity<?> assignRole(@PathVariable String bookId,
                                        @RequestBody RoleAssignment request,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        if (!bookId.equals(request.getBookId())) {

            System.out.println("El ID del libro en la URL no coincide con el ID del libro en el cuerpo de la solicitud." + " Book ID: " + bookId + ", Request Book ID: " + request.getBookId());
            return ResponseEntity.badRequest().body("El ID del libro en la URL no coincide con el ID del libro en el cuerpo de la solicitud.");
        }
        User currentUser = securityUtils.getCurrentUser(userDetails);

        BookPage book = bookPageService.getBookPageById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
        UserDTO user = userService.getUserById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Validación de permisos
        Optional<RoleAssignment> currentUserRole = roleAssignmentService.findByBookIdAndUserId(bookId, currentUser.getId());

        if (currentUserRole.isEmpty() || !currentUserRole.get().getRole().equals("OWNER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado");
        }

        // Validación de rol válido
        if (!VALID_ROLES.contains(request.getRole())) {
            return ResponseEntity.badRequest().body("Rol no válido");
        }

        // Si ya existe, actualizamos
        Optional<RoleAssignment> existing = roleAssignmentService
                .findByBookIdAndUserId(bookId, request.getUserId());


        if (existing.isPresent()) {
            RoleAssignment role = existing.get();

            if (existing.get().getRole().equals("OWNER") && existing.get().getUserId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No puedes eliminarte a ti mismo como OWNER.");
        }

            role.setRole(request.getRole());
            roleAssignmentService.saveRole(role);
            return ResponseEntity.ok(role);
        }

        // Si no existe, creamos nuevo
        request.setId(UUID.randomUUID().toString());
        roleAssignmentService.saveRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @GetMapping("/{bookId}")
    public Optional<RoleAssignment> getUserRolesForBook(@PathVariable String bookId,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return roleAssignmentService.findByBookIdAndUserId(bookId, user.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public List<RoleAssignment> getRolesForUser(@PathVariable String userId) {
        return roleAssignmentService.findById(userId);
    }

    @GetMapping("/me")
    public List<RoleAssignment> getRolesForCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = securityUtils.getCurrentUser(userDetails);
        return roleAssignmentService.findById(currentUser.getId());
    }

    @DeleteMapping("/{bookId}/{userId}")
    public ResponseEntity<?> removeRole(@PathVariable String bookId,
                                        @PathVariable String userId,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = securityUtils.getCurrentUser(userDetails);

        Optional<RoleAssignment> currentUserRole = roleAssignmentService
                .findByBookIdAndUserId(bookId, currentUser.getId());

        if (currentUserRole.isEmpty() || !currentUserRole.get().getRole().equals("OWNER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado");
        }

        Optional<RoleAssignment> existing = roleAssignmentService
                .findByBookIdAndUserId(bookId, userId);

        if (existing.get().getRole().equals("OWNER") &&
                existing.get().getUserId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No puedes eliminarte a ti mismo como OWNER.");
        }

        if (existing.isPresent()) {
            roleAssignmentService.deleteById(existing.get().getId());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/book/{bookId}")
    public List<RoleAssignment> getRolesForBook(@PathVariable String bookId,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = securityUtils.getCurrentUser(userDetails);
        Optional<RoleAssignment> currentRole = roleAssignmentService.findByBookIdAndUserId(bookId, currentUser.getId());

        if (currentRole.isEmpty() || !currentRole.get().getRole().equals("OWNER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }

        return roleAssignmentService.findAllByBookId(bookId);
    }

}
