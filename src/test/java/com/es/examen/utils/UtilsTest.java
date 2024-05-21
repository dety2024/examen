package com.es.examen.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilsTest {

    @Test
    void testCalculateAgeSuccess() {
        LocalDate fechaNacimiento = LocalDate.now().minusYears(25);

        int edad = Utils.calculateAge(fechaNacimiento);

        assertEquals(25, edad);
    }

    @Test
    void testCalculateAgeFechaNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Utils.calculateAge(null);
        });

        assertEquals("La fecha de nacimiento no puede ser nula", exception.getMessage());
    }

    @Test
    void testCalculateAgeFechaFutura() {
        LocalDate fechaFutura = LocalDate.now().plusYears(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Utils.calculateAge(fechaFutura);
        });

        assertEquals("La fecha de nacimiento no puede ser en el futuro", exception.getMessage());
    }

}
