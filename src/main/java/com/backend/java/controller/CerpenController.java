package com.backend.java.controller;

import com.backend.java.domain.model.CerpenCreateRequestDTO;
import com.backend.java.domain.model.ResponseData;
import com.backend.java.midleware.JwtClaimsService;
import com.backend.java.service.CerpenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/cerpen")
public class CerpenController {
    @Autowired
    private JwtClaimsService jwtClaimsService;
    private final CerpenService cerpenService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('author')")
    public ResponseEntity<ResponseData<String>> createCerpen(
            @RequestHeader(name="Authorization") String token,
            @RequestBody @Valid CerpenCreateRequestDTO dto, Errors errors) {
        ResponseData<String> responseData = new ResponseData<>();
        var username = jwtClaimsService.extractUsername(token);

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }

            responseData.setCode(HttpStatus.BAD_REQUEST.value());
            responseData.setStatus(HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        cerpenService.createNewCerpen(username, dto);

        responseData.setCode(HttpStatus.CREATED.value());
        responseData.setStatus(HttpStatus.CREATED);
        responseData.setMessage(Collections.singletonList("Success Create Cerpen"));
        return ResponseEntity.created(URI.create("/cerper/create")).body(responseData);
    }
}