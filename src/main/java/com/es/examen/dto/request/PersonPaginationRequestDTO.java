package com.es.examen.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonPaginationRequestDTO {

    private String search;
    private int pageNumber;
    private int pageSize;


}
