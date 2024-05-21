package com.es.examen.service;

import com.es.examen.dto.request.PersonCreateRequestDTO;
import com.es.examen.dto.request.PersonEditRequestDTO;
import com.es.examen.dto.request.PersonPaginationRequestDTO;
import com.es.examen.dto.response.ApiResponse;
import com.es.examen.dto.response.ApiResponsePagination;
import com.es.examen.dto.response.PersonResponseDTO;
import com.es.examen.exception.ApplicationException;

import java.util.List;

public interface PersonService {

    ApiResponse<Void> createPersonService(PersonCreateRequestDTO requestDTO) throws ApplicationException;
    ApiResponse<PersonResponseDTO> listByIdPersonService(Long id) throws ApplicationException;
    ApiResponse<Void> updatePersonService(PersonEditRequestDTO requestDTO) throws ApplicationException;
    ApiResponsePagination<List<PersonResponseDTO>> listPaginatedPersonService(PersonPaginationRequestDTO requestDTO) throws ApplicationException;

}
