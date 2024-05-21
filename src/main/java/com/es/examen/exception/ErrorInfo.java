package com.es.examen.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class ErrorInfo {

    private Date timestamp;
    private String path;
    private HttpStatus status;
    private List<Object> listMessage;
    private String message;

}