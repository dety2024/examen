package com.es.examen.controller;

import com.es.examen.dto.request.PersonCreateRequestDTO;
import com.es.examen.dto.request.PersonEditRequestDTO;
import com.es.examen.dto.request.PersonPaginationRequestDTO;
import com.es.examen.dto.response.ApiResponse;
import com.es.examen.dto.response.ApiResponsePagination;
import com.es.examen.dto.response.PersonResponseDTO;
import com.es.examen.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private PersonCreateRequestDTO requestDTO;
    private ApiResponse<PersonResponseDTO> apiResponse;
    private PersonPaginationRequestDTO personPaginationRequestDTO;
    PersonEditRequestDTO requestEditDTO;

    @BeforeEach
    public void setUp() {
        requestDTO = new PersonCreateRequestDTO();
        requestDTO.setNombre("Juan");
        requestDTO.setApellido("Perez");
        requestDTO.setFecha_nacimiento(LocalDate.of(1990, 1, 1));
        requestDTO.setDni("12345678");

        PersonResponseDTO personResponseDTO = new PersonResponseDTO();
        personResponseDTO.setId(1L);
        personResponseDTO.setNombre("Juan");
        personResponseDTO.setApellido("Perez");

        apiResponse = ApiResponse.<PersonResponseDTO>builder()
                .status(HttpStatus.OK)
                .message("Usuario encontrado correctamente.")
                .data(personResponseDTO)
                .build();

        requestEditDTO = new PersonEditRequestDTO();
        requestEditDTO.setId(1L);
        requestEditDTO.setNombre("Juan");
        requestEditDTO.setApellido("Perez");
        requestEditDTO.setFecha_nacimiento(LocalDate.of(1990, 1, 1));
        requestEditDTO.setDni("12345678");

        personPaginationRequestDTO = new PersonPaginationRequestDTO();
        personPaginationRequestDTO.setPageNumber(0);
        personPaginationRequestDTO.setPageSize(10);
        personPaginationRequestDTO.setSearch("example");

    }

    @Test
    void testPersonCreateControllerSuccess() throws Exception {
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.CREATED)
                .message("Persona Creada correctamente.")
                .build();

        Mockito.when(personService.createPersonService(Mockito.any(PersonCreateRequestDTO.class)))
                .thenReturn(apiResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/prueba/persona")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.message").value("Persona Creada correctamente."));
    }

    @Test
    void testPersonListByIdSuccess() throws Exception {
        Mockito.when(personService.listByIdPersonService(1L)).thenReturn(apiResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/prueba/persona/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Usuario encontrado correctamente."))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.nombre").value("Juan"))
                .andExpect(jsonPath("$.data.apellido").value("Perez"));
    }

    @Test
    void testPersonUpdateControllerSuccess() throws Exception {
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Persona actualizada correctamente.")
                .build();

        Mockito.when(personService.updatePersonService(Mockito.any(PersonEditRequestDTO.class)))
                .thenReturn(apiResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/prueba/persona")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestEditDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Persona actualizada correctamente."));
    }

    @Test
    void testPersonListControllerSuccess() throws Exception {
        ApiResponsePagination<List<PersonResponseDTO>> apiResponse = ApiResponsePagination.<List<PersonResponseDTO>>builder()
                .status(HttpStatus.OK)
                .message("Personas listadas correctamente")
                .data(Arrays.asList(new PersonResponseDTO(), new PersonResponseDTO()))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(2)
                .totalPages(1)
                .build();

        Mockito.when(personService.listPaginatedPersonService(Mockito.any(PersonPaginationRequestDTO.class)))
                .thenReturn(apiResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/prueba/persona/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personPaginationRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Personas listadas correctamente"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

}
