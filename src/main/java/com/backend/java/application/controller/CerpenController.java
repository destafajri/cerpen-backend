package com.backend.java.application.controller;

import com.backend.java.application.dto.CerpenCreateRequestDTO;
import com.backend.java.application.dto.CerpenListByIdRequestDTO;
import com.backend.java.application.dto.CerpenResponseDTO;
import com.backend.java.application.security.JwtClaimsService;
import com.backend.java.application.service.CerpenService;
import com.backend.java.domain.model.MetadataResponse;
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

import java.util.List;

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

    @PostMapping(
            path = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseData<List<CerpenResponseDTO>>> getListCerpenByIds(
            @RequestParam(name = "page", required = true, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "per_page", required = true, defaultValue = "10") Integer limit,
            @RequestParam(name = "sort_by", required = true, defaultValue = "created_at") String sortBy,
            @RequestParam(name = "order_type", required = true, defaultValue = "asc") String sortOrder,
            @RequestBody CerpenListByIdRequestDTO dto) {
        var data = cerpenService.getListCerpenById(dto, pageNumber, limit, sortBy, sortOrder);

        return new ResponseEntity<>(ResponseData.<List<CerpenResponseDTO>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("Succes Get List Cerpen")
                .metadata(MetadataResponse.builder()
                        .page(pageNumber)
                        .perPage(limit)
                        .total(data.size())
                        .sortBy(sortBy)
                        .orderType(sortOrder)
                        .build())
                .data(data)
                .build(),
                HttpStatus.OK);
    }
}