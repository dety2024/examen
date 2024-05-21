package com.es.examen.service.impl;

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
import com.es.examen.service.PersonService;
import com.es.examen.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public ApiResponse<Void> createPersonService(PersonCreateRequestDTO requestDTO) throws ApplicationException {

        Optional<Person> personFind = personRepository.findByDni(requestDTO.getDni());

        if(personFind.isPresent()){
            throw new ApplicationException(HttpStatus.BAD_REQUEST, String.format("%s %s", "Ya existe el usuario con el dni", requestDTO.getDni()));
        }

        requestDTO.setEdad(Utils.calculateAge(requestDTO.getFecha_nacimiento()));

        personRepository.save(personMapper.toEntity(requestDTO));

        return ApiResponse
                .<Void>builder()
                .status(HttpStatus.CREATED)
                .message("Persona Creada correctamente.")
                .build();
    }

    @Override
    public ApiResponse<PersonResponseDTO> listByIdPersonService(Long id) throws ApplicationException {

        Optional<Person> findPerson = personRepository.findById(id);
        if(!findPerson.isPresent()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, String.format("%s %d", "No se encontro a la persona con id", id));
        }

        return ApiResponse
                .<PersonResponseDTO>builder()
                .data(personMapper.toDTO(findPerson.get()))
                .message("Usuario encontrado correctamente.")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ApiResponse<Void> updatePersonService(PersonEditRequestDTO requestDTO) throws ApplicationException {

        Optional<Person> findPerson = personRepository.findById(requestDTO.getId());
        if (!findPerson.isPresent()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, String.format("No se encontró a la persona con id %d", requestDTO.getId()));
        }

        Optional<Person> findPersonByDni = personRepository.findByDni(requestDTO.getDni());
        if (findPersonByDni.isPresent() && !findPersonByDni.get().getId().equals(requestDTO.getId())) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, String.format("Ya existe una persona con el DNI %s", requestDTO.getDni()));
        }

        requestDTO.setEdad(Utils.calculateAge(requestDTO.getFecha_nacimiento()));

        personRepository.save(personMapper.toEntity(requestDTO));

        return ApiResponse
                .<Void>builder()
                .status(HttpStatus.OK)
                .message("Persona actualizada correctamente.")
                .build();
    }

    @Override
    public ApiResponsePagination<List<PersonResponseDTO>> listPaginatedPersonService(PersonPaginationRequestDTO requestDTO) throws ApplicationException {

        Pageable pageable = PageRequest.of(requestDTO.getPageNumber(), requestDTO.getPageSize());
        Page<Person> listPerson = personRepository.findByNombreContaining(requestDTO.getSearch(), pageable);

        List<PersonResponseDTO> listaPersonas = listPerson.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());

        return ApiResponsePagination
                .<List<PersonResponseDTO>>builder()
                .data(listaPersonas)
                .status(HttpStatus.OK)
                .message("Personas listadas correctamente")
                .pageNumber(listPerson.getNumber())
                .pageSize(listPerson.getSize())
                .totalElements(listPerson.getTotalElements())
                .totalPages(listPerson.getTotalPages())
                .build();
    }
}
