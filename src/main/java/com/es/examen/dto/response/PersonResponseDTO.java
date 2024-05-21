package com.es.examen.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PersonResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fecha_nacimiento;
    private String dni;
    private int edad;

}
