package com.backend.java.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class CerpenCreateRequestDTO {

    @NotBlank(message = "title must not be blank")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters long")
    private String title;
    @NotNull(message = "tema must not be null")
    private TemaEnum tema;
    @NotBlank(message = "cerpen contains must not be blank")
    @Size(min = 10, max = 1000, message = "Cerpen must be between 10 and 1000 characters long")
    private String cerpenContains;

}