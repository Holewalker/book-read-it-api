package com.svalero.bookreaditapi.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "El nombre es obligatorio")
    private String name;

    private String description;

    @NotBlank(message = "La categoria no puede estar vacia")
    @NotNull(message = "La categoria es obligatoria")
    private String category;

}
