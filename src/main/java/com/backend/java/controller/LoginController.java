package com.backend.java.controller;

import com.backend.java.domain.model.LoginRequestDTO;
import com.backend.java.domain.model.ResponseData;
import com.backend.java.midleware.JwtClaimsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class LoginController {

    @Autowired
    private JwtClaimsService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(
            path = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseData<String>> authenticateAndGetToken(
            @RequestBody @Valid LoginRequestDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                        authRequest.getPassword()));

        var token = authentication.isAuthenticated() ?
                jwtService.generateToken(authRequest.getUsername()) :
                null;
        if (token == null) {
            throw new UsernameNotFoundException("invalid user request !");
        }

        return new ResponseEntity<>(ResponseData.<String>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("Login Success")
                .token(token).build(),
                HttpStatus.OK);
    }
}
