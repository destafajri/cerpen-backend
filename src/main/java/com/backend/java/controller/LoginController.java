package com.backend.java.controller;

import com.backend.java.domain.model.LoginRequestDTO;
import com.backend.java.domain.model.ResponseData;
import com.backend.java.midleware.JwtClaimsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Slf4j
@RestController
@AllArgsConstructor
public class LoginController {

    @Autowired
    private JwtClaimsService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<ResponseData<String>> authenticateAndGetToken(
            @RequestBody @Valid LoginRequestDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                        authRequest.getPassword()));
        ResponseData responseData = new ResponseData<>();

        var token = authentication.isAuthenticated() ?
                jwtService.generateToken(authRequest.getUsername()) :
                null;

        if (token == null) {
            throw new UsernameNotFoundException("invalid user request !");
        }

        responseData.setCode(HttpStatus.OK.value());
        responseData.setStatus(HttpStatus.OK);
        responseData.setMessage(Collections.singletonList("login Success"));
        responseData.setToken(token);
        return ResponseEntity.ok(responseData);
    }
}
