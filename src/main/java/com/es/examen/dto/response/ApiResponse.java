package com.es.examen.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ApiResponse <T> {

    private HttpStatus status;
    private String message;
    private T data;

}
