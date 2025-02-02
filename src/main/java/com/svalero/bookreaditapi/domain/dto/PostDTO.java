package com.svalero.bookreaditapi.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    @NotNull
    private long userId;
    @NotNull
    private long bookId;
    @NotBlank(message = "El comentario no puede estar vacio")
    @NotNull(message = "El comentario es obligatorio")
    private String comment;

    private LocalDate date;
}
