package com.es.examen.utils;

import java.time.LocalDate;
import java.time.Period;

public class Utils {

    public static int calculateAge(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
        }

        LocalDate currentDate = LocalDate.now();
        if (fechaNacimiento.isAfter(currentDate)) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser en el futuro");
        }

        return Period.between(fechaNacimiento, currentDate).getYears();
    }

}
