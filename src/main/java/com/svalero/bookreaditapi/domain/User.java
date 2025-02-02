package com.svalero.bookreaditapi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.ToString;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(unique = true)
    @NotBlank(message = "El nombre de usuario no puede estar vacio")
    @NotNull(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacia")
    @NotNull(message = "La contraseña es obligatoria")
    private String password;

    @Column
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @NotNull(message = "El correo electrónico es obligatorio")
    private String email;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ToString.Exclude
    @OneToMany(mappedBy = "userPost")
    @JsonBackReference(value = "post_id")
    private List<Post> posts;

    @ToString.Exclude
    @OneToMany(mappedBy = "userReply")
    @JsonBackReference(value = "reply_id")
    private List<Reply> replies;


}