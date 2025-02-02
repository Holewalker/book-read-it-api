package com.svalero.bookreaditapi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @NotNull(message = "El nombre es obligatorio")
    @NotBlank(message = "El nombre no puede estar vacio")
    private String name;
    @Column
    @NotNull(message = "La descripcion es obligatoria")
    @NotBlank(message = "La descripcion no puede estar vacia")
    private String description;
    @Column
    private String category;

    @ToString.Exclude
    @OneToMany(mappedBy = "bookPost")
    @JsonBackReference(value = "post_id")
    private List<Post> posts;
}
