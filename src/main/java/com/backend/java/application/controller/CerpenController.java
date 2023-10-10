package com.backend.java.application.controller;

import com.backend.java.application.dto.CerpenCreateRequestDTO;
import com.backend.java.application.security.JwtClaimsService;
import com.backend.java.application.service.CerpenService;
import com.backend.java.domain.model.ResponseData;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/cerpen")
public class CerpenController {
    @Autowired
    private JwtClaimsService jwtClaimsService;
    private final CerpenService cerpenService;

    @PostMapping(
            path = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('AUTHOR')")
    public ResponseEntity<ResponseData<String>> createCerpen(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody @Valid CerpenCreateRequestDTO dto) {
        var username = jwtClaimsService.extractUsername(token);
        cerpenService.createNewCerpen(username, dto);

        return new ResponseEntity<>(ResponseData.<String>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .message("Succes Create New Cerpen")
                .build(),
                HttpStatus.CREATED);
    }
}