package com.es.examen.repository;

import com.es.examen.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, Long> {

    Optional<Person> findByDni(String dni);
    Page<Person> findByNombreContaining(String nombre, Pageable pageable);

}
