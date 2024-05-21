package com.es.examen.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Setter
@Getter
public class PersonCreateRequestDTO {

    @NotEmpty
    private String nombre;

    @NotEmpty
    private String apellido;

    @NotNull
    private LocalDate fecha_nacimiento;

    @NotEmpty
    @Size(min = 8, max = 8, message = "El tamaño debe de ser de 8")
    private String dni;

    private int edad;

}
