package com.backend.java.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class LoginRequestDTO {
    @NotBlank(message = "username must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$", message = "must be username format")
    private String username;
    @NotBlank(message = "password must not be blank")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters long")
    private String password;
}