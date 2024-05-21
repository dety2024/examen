package com.es.examen.exception.handler;

import com.es.examen.exception.ApplicationException;
import com.es.examen.exception.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handleMethodArgumentNotValid(HttpServletRequest request,
                                                                                MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<Object> listError = new ArrayList<>();

        fieldErrors.forEach(f -> {
            Map<String, String> mapError = new HashMap<>();
            mapError.put("field", f.getField());
            mapError.put("alert", f.getDefaultMessage());
            listError.add(mapError);
        });

        ErrorInfo errorInfo = ErrorInfo.builder()
                .timestamp(new Date())
                .path(request.getRequestURI())
                .status(HttpStatus.BAD_REQUEST)
                .listMessage(listError)
                .message("")
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorInfo> badRequestException(final ApplicationException e, final HttpServletRequest request) {

        ErrorInfo errorInfo = ErrorInfo.builder()
                .status(e.getStatus())
                .timestamp(new Date())
                .path(request.getRequestURI())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, e.getStatus());
    }

}