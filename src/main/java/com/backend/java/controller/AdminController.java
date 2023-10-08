package com.backend.java.controller;

import com.backend.java.domain.model.AdminCreateRequestDTO;
import com.backend.java.domain.model.ResponseData;
import com.backend.java.service.AdminService;
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
@RequestMapping("/auth/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping(
            path = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseData<String>> adminCreate(
            @RequestBody @Valid AdminCreateRequestDTO dto) {
        adminService.createNewAdmin(dto);

        return new ResponseEntity<>(ResponseData.<String>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .message("Succes Create New Admin")
                .build(),
                HttpStatus.CREATED);
    }
}
