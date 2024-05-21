package com.es.examen.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

}
