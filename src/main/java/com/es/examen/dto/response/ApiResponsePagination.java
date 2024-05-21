package com.es.examen.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
public class ApiResponsePagination <T> {

    private HttpStatus status;
    private String message;
    private T data;
    private int pageSize;
    private int pageNumber;
    private int totalPages;
    private long totalElements;

}
