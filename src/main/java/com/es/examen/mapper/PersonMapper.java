package com.es.examen.mapper;

import com.es.examen.dto.request.PersonCreateRequestDTO;
import com.es.examen.dto.request.PersonEditRequestDTO;
import com.es.examen.dto.response.PersonResponseDTO;
import com.es.examen.entity.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    Person toEntity(PersonCreateRequestDTO requestDTO);
    Person toEntity(PersonEditRequestDTO requestDTO);
    PersonResponseDTO toDTO(Person person);

}
