package com.es.examen.controller;

import com.es.examen.dto.request.PersonCreateRequestDTO;
import com.es.examen.dto.request.PersonEditRequestDTO;
import com.es.examen.dto.request.PersonPaginationRequestDTO;
import com.es.examen.dto.response.ApiResponse;
import com.es.examen.dto.response.ApiResponsePagination;
import com.es.examen.dto.response.PersonResponseDTO;
import com.es.examen.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/prueba/persona")
@RequiredArgsConstructor
@Validated
public class PersonController {

    private final PersonService personService;

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ApiResponse<Void>> personCreateController(@Valid @RequestBody PersonCreateRequestDTO requestDTO) {
        return new ResponseEntity<>(personService.createPersonService(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping(
            produces = { MediaType.APPLICATION_JSON_VALUE },
            path = "/{idPerson}"
    )
    public ResponseEntity<ApiResponse<PersonResponseDTO>> personListById(@PathVariable Long idPerson) {
        return new ResponseEntity<>(personService.listByIdPersonService(idPerson), HttpStatus.OK);
    }

    @PutMapping(
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<ApiResponse<Void>> personUpdateController(@Valid @RequestBody PersonEditRequestDTO requestDTO) {
        return new ResponseEntity<>(personService.updatePersonService(requestDTO), HttpStatus.OK);
    }

    @GetMapping(
            produces = { MediaType.APPLICATION_JSON_VALUE },
            path = "/list"
    )
    public ResponseEntity<ApiResponsePagination<List<PersonResponseDTO>>> personListController(PersonPaginationRequestDTO requestDTO) {
        return new ResponseEntity<>(personService.listPaginatedPersonService(requestDTO), HttpStatus.OK);
    }

}
