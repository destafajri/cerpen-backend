package com.backend.java.exception;

import com.backend.java.domain.model.ResponseData;
import com.backend.java.utility.CurrentTimeStamp;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseData<String>> apiException(ResponseStatusException exception) {
        return new ResponseEntity<>(ResponseData.<String>builder()
                .code(exception.getStatusCode().value())
                .status((HttpStatus) exception.getStatusCode())
                .error(exception.getMessage())
                .messageError(Collections.singletonList(exception.getReason()))
                .requestOn(CurrentTimeStamp.getLocalDateTime())
                .build(),
                exception.getStatusCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseData<String>> constraintViolationException(ConstraintViolationException exception) {
        var joinedErrors = exception.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .sorted()
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ResponseData.<String>builder()
                        .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .error("Invalid input")
                        .messageError(joinedErrors)
                        .requestOn(CurrentTimeStamp.getLocalDateTime())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData<String>> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var fieldErrors = exception.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .sorted()
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ResponseData.<String>builder()
                        .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .error("Invalid input")
                        .messageError(fieldErrors)
                        .requestOn(CurrentTimeStamp.getLocalDateTime())
                        .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseData<String>> dataIntegrityViolationException(DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseData.<String>builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST)
                        .error("Data already exist")
                        .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseData<String>> badCredentialsException(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseData.<String>builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .status(HttpStatus.UNAUTHORIZED)
                        .error("Username or password isn't match")
                        .messageError(Collections.singletonList(exception.getMessage()))
                        .requestOn(CurrentTimeStamp.getLocalDateTime())
                        .build());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ResponseData<String>> jwtException(JwtException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseData.<String>builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .status(HttpStatus.UNAUTHORIZED)
                        .error("Invalid JWT")
                        .messageError(Collections.singletonList(exception.getMessage()))
                        .build());
    }
}
