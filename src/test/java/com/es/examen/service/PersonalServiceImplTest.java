package com.es.examen.service;

import com.es.examen.dto.request.PersonCreateRequestDTO;
import com.es.examen.dto.request.PersonEditRequestDTO;
import com.es.examen.dto.request.PersonPaginationRequestDTO;
import com.es.examen.dto.response.ApiResponse;
import com.es.examen.dto.response.ApiResponsePagination;
import com.es.examen.dto.response.PersonResponseDTO;
import com.es.examen.entity.Person;
import com.es.examen.exception.ApplicationException;
import com.es.examen.mapper.PersonMapper;
import com.es.examen.repository.PersonRepository;
import com.es.examen.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class PersonalServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonServiceImpl personService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePersonServiceSuccess() throws ApplicationException {

        PersonCreateRequestDTO requestDTO = new PersonCreateRequestDTO();
        requestDTO.setDni("12345678");
        requestDTO.setNombre("Juan");
        requestDTO.setApellido("Perez");
        requestDTO.setFecha_nacimiento(LocalDate.of(1990, 1, 1));

        Person person = new Person();
        person.setDni("12345678");
        person.setNombre("Juan");
        person.setApellido("Perez");
        person.setFecha_nacimiento(LocalDate.of(1990, 1, 1));

        when(personRepository.findByDni(anyString())).thenReturn(Optional.empty());
        when(personMapper.toEntity(any(PersonCreateRequestDTO.class))).thenReturn(person);

        ApiResponse<Void> response = personService.createPersonService(requestDTO);

        assertEquals("Persona Creada correctamente.", response.getMessage());
    }

    @Test
    void testCreatePersonServiceDniExists() {
        // Datos de prueba
        PersonCreateRequestDTO requestDTO = new PersonCreateRequestDTO();
        requestDTO.setDni("12345678");
        requestDTO.setNombre("Juan");
        requestDTO.setApellido("Perez");
        requestDTO.setFecha_nacimiento(LocalDate.of(1990, 1, 1));

        Person person = new Person();
        person.setDni("12345678");

        when(personRepository.findByDni(anyString())).thenReturn(Optional.of(person));

        ApplicationException exception = assertThrows(ApplicationException.class, () -> personService.createPersonService(requestDTO));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Ya existe el usuario con el dni 12345678", exception.getMessage());
    }

    @Test
    void testListByIdPersonServiceSuccess() throws ApplicationException {

        Long id = 1L;
        Person person = new Person();
        person.setId(id);
        person.setNombre("Juan");
        person.setApellido("Perez");

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));

        PersonResponseDTO personResponseDTO = new PersonResponseDTO();
        personResponseDTO.setId(id);
        personResponseDTO.setNombre("Juan");
        personResponseDTO.setApellido("Perez");

        when(personMapper.toDTO(person)).thenReturn(personResponseDTO);

        ApiResponse<PersonResponseDTO> response = personService.listByIdPersonService(id);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Usuario encontrado correctamente.", response.getMessage());
        assertEquals(id, response.getData().getId());
        assertEquals("Juan", response.getData().getNombre());
        assertEquals("Perez", response.getData().getApellido());
    }

    @Test
    void testListByIdPersonServiceNotFound() {
        // Datos de prueba
        Long id = 1L;

        when(personRepository.findById(anyLong())).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> personService.listByIdPersonService(id));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("No se encontro a la persona con id 1", exception.getMessage());
    }

    @Test
    void testUpdatePersonServiceSuccess() throws ApplicationException {

        Long id = 1L;
        String dni = "12345678";
        LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);

        PersonEditRequestDTO requestDTO = new PersonEditRequestDTO();
        requestDTO.setId(id);
        requestDTO.setDni(dni);
        requestDTO.setNombre("Juan");
        requestDTO.setApellido("Perez");
        requestDTO.setFecha_nacimiento(fechaNacimiento);

        Person existingPerson = new Person();
        existingPerson.setId(id);
        existingPerson.setDni(dni);
        existingPerson.setNombre("Juan");
        existingPerson.setApellido("Perez");
        existingPerson.setFecha_nacimiento(fechaNacimiento);

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(existingPerson));
        when(personRepository.findByDni(anyString())).thenReturn(Optional.of(existingPerson));

        Person updatedPerson = new Person();
        updatedPerson.setId(id);
        updatedPerson.setDni(dni);
        updatedPerson.setNombre("Juan");
        updatedPerson.setApellido("Perez");
        updatedPerson.setFecha_nacimiento(fechaNacimiento);

        when(personMapper.toEntity(any(PersonEditRequestDTO.class))).thenReturn(updatedPerson);
        when(personRepository.save(any(Person.class))).thenReturn(updatedPerson);

        ApiResponse<Void> response = personService.updatePersonService(requestDTO);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Persona actualizada correctamente.", response.getMessage());
    }

    @Test
    void testUpdatePersonServicePersonNotFound() {

        Long id = 1L;
        String dni = "12345678";

        PersonEditRequestDTO requestDTO = new PersonEditRequestDTO();
        requestDTO.setId(id);
        requestDTO.setDni(dni);

        when(personRepository.findById(anyLong())).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> personService.updatePersonService(requestDTO));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("No se encontró a la persona con id 1", exception.getMessage());
    }

    @Test
    void testUpdatePersonServiceDniExists() {

        Long id = 1L;
        Long otherId = 2L;
        String dni = "12345678";

        PersonEditRequestDTO requestDTO = new PersonEditRequestDTO();
        requestDTO.setId(id);
        requestDTO.setDni(dni);

        Person existingPerson = new Person();
        existingPerson.setId(otherId);
        existingPerson.setDni(dni);

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(new Person()));
        when(personRepository.findByDni(anyString())).thenReturn(Optional.of(existingPerson));

        ApplicationException exception = assertThrows(ApplicationException.class, () -> personService.updatePersonService(requestDTO));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Ya existe una persona con el DNI 12345678", exception.getMessage());
    }

    @Test
    void testListPaginatedPersonServiceSuccess() throws ApplicationException {

        PersonPaginationRequestDTO requestDTO = new PersonPaginationRequestDTO();
        requestDTO.setPageNumber(0);
        requestDTO.setPageSize(10);
        requestDTO.setSearch("Juan");

        Person person = new Person();
        person.setId(1L);
        person.setNombre("Juan");
        person.setApellido("Perez");

        PersonResponseDTO personResponseDTO = new PersonResponseDTO();
        personResponseDTO.setId(1L);
        personResponseDTO.setNombre("Juan");
        personResponseDTO.setApellido("Perez");

        List<Person> personList = Collections.singletonList(person);
        Page<Person> pagePerson = new PageImpl<>(personList);

        when(personRepository.findByNombreContaining(anyString(), any(Pageable.class))).thenReturn(pagePerson);
        when(personMapper.toDTO(person)).thenReturn(personResponseDTO);

        ApiResponsePagination<List<PersonResponseDTO>> response = personService.listPaginatedPersonService(requestDTO);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Personas listadas correctamente", response.getMessage());
        assertEquals(1, response.getData().size());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
    }

    @Test
    void testListPaginatedPersonServiceNoResults() throws ApplicationException {

        PersonPaginationRequestDTO requestDTO = new PersonPaginationRequestDTO();
        requestDTO.setPageNumber(0);
        requestDTO.setPageSize(10);
        requestDTO.setSearch("Nonexistent");

        Page<Person> pagePerson = new PageImpl<>(Collections.emptyList());

        when(personRepository.findByNombreContaining(anyString(), any(Pageable.class))).thenReturn(pagePerson);

        ApiResponsePagination<List<PersonResponseDTO>> response = personService.listPaginatedPersonService(requestDTO);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Personas listadas correctamente", response.getMessage());
        assertEquals(0, response.getData().size());
        assertEquals(0, response.getTotalElements());

    }

}
