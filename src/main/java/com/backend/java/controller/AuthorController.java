package com.backend.java.controller;

import com.backend.java.domain.model.AuthorCreateRequestDTO;
import com.backend.java.domain.model.ResponseData;
import com.backend.java.service.AuthorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collections;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping("/create")
    public ResponseEntity<ResponseData<String>> authorCreate(
            @RequestBody @Valid AuthorCreateRequestDTO dto, Errors errors) {
        ResponseData responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }

            responseData.setCode(HttpStatus.BAD_REQUEST.value());
            responseData.setStatus(HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        authorService.createNewAuthor(dto);

        responseData.setCode(HttpStatus.CREATED.value());
        responseData.setStatus(HttpStatus.CREATED);
        responseData.setMessage(Collections.singletonList("Success Create New Author"));
        return ResponseEntity.created(URI.create("/author/create")).body(responseData);
    }

}
