package com.backend.java.application.controller;

import com.backend.java.application.dto.AuthorCreateRequestDTO;
import com.backend.java.application.service.AuthorService;
import com.backend.java.domain.model.ResponseData;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth/author")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping(
            path = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseData<String>> authorCreate(
            @RequestBody @Valid AuthorCreateRequestDTO dto) {
        authorService.createNewAuthor(dto);

        return new ResponseEntity<>(ResponseData.<String>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .message("Succes Create New Author")
                .build(),
                HttpStatus.CREATED);
    }
}
