package com.backend.java.controller;

import com.backend.java.domain.model.CerpenCreateRequestDTO;
import com.backend.java.domain.model.ResponseData;
import com.backend.java.midleware.JwtClaimsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/cerpen")
public class CerpenController {
    @Autowired
    private JwtClaimsService jwtClaimsService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('author')")
    public ResponseEntity<ResponseData<String>> createCerpen(
            @RequestHeader(name="Authorization") String token,
            @RequestBody @Valid CerpenCreateRequestDTO dto, Errors errors) {
        var username = jwtClaimsService.extractUsername(token);

        return null;
    }
}
